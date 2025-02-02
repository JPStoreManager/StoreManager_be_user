package manage.store.service.login;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import manage.store.DTO.entity.User;
import manage.store.DTO.login.LoginRequest;
import manage.store.DTO.login.LoginResponse;
import manage.store.consts.SuccessFlag;
import manage.store.consts.Tags;
import manage.store.repository.UserAccountRepository;
import manage.store.testUtils.DtoValidationUtil;
import manage.store.testUtils.UserUtils;
import manage.store.utils.SecretUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

import static org.assertj.core.api.Assertions.*;

@Tag(Tags.Test.UNIT)
@ExtendWith({MockitoExtension.class})
class LoginServiceImplTest {

    /** 타겟 외 임시로 given-then이 주어질 객체 */
    @Mock
    private UserAccountRepository userAccountRepository;

    /** 실제 테스트 타겟 객체*/
    @InjectMocks
    private LoginServiceImpl loginService;

    private static Validator validator;

    @BeforeAll
    static void setup() {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.afterPropertiesSet();
        validator = factoryBean;
    }

    /** login */
    @Test
    @DisplayName("login 성공")
    void loginTest_success() {
        // Given
        final LoginRequest request = new LoginRequest("userId1", "password123");

        User user = UserUtils.createUser(request.getId());
        given(userAccountRepository.selectUserById(user.getId())).willReturn(user);

        try(MockedStatic<SecretUtils> SecretUtilsMock = mockStatic(SecretUtils.class)) {
            SecretUtilsMock.when(() -> SecretUtils.verify(request.getPassword(), user.getPassword()))
                    .thenReturn(true);

            // When
            LoginResponse response = loginService.login(request);

            // Then
            assertThat(response.getResult()).isEqualTo(SuccessFlag.Y);
        }
    }

    @Test
    @DisplayName("login 실패 - 존재하지 않는 사용자")
    void loginTest_fail_UserNotExist() {
        // Given
        final LoginRequest request = new LoginRequest("userId1", "password123");

        given(userAccountRepository.selectUserById(request.getId())).willReturn(null);

        // When
        LoginResponse response = loginService.login(request);

        // Then
        assertThat(response.getResult()).isEqualTo(SuccessFlag.N);
    }

    @Test
    @DisplayName("login 실패 - 비밀번호 불일치")
    void loginTest_fail_IncorrectPassword() {
        // Given
        final LoginRequest request = new LoginRequest("userId1", "password123");

        User user = UserUtils.createUser(request.getId());
        given(userAccountRepository.selectUserById(user.getId())).willReturn(user);

        try(MockedStatic<SecretUtils> SecretUtilsMock = mockStatic(SecretUtils.class)) {
            SecretUtilsMock.when(() -> SecretUtils.verify(request.getPassword(), user.getPassword()))
                    .thenReturn(false);

            // When
            LoginResponse response = loginService.login(request);

            // Then
            assertThat(response.getResult()).isEqualTo(SuccessFlag.N);
        }
    }

    @Test
    @DisplayName("login 실패 - 유효하지 않은 parameter(id)")
    void loginTest_fail_invalidParameter_id() {
        // Given1
        final LoginRequest request = new LoginRequest("", "password123");

        // When1
        Set<ConstraintViolation<LoginRequest>> result = validator.validate(request);

        // Then1
        Assertions.assertThat(result.isEmpty()).isFalse();
        Assertions.assertThat(result.iterator().next().getPropertyPath().toString()).isEqualTo("id");
    }

    @Test
    @DisplayName("login 실패 - 유효하지 않은 parameter(password)")
    void loginTest_fail_invalidParameter_password() {
        // Given2
        final LoginRequest request = new LoginRequest("userId1", "");

        // When2
        Set<ConstraintViolation<LoginRequest>> result = validator.validate(request);

        // Then2
        Assertions.assertThat(result.isEmpty()).isFalse();
        Assertions.assertThat(result.iterator().next().getPropertyPath().toString()).isEqualTo("password");
    }

    @Test
    @DisplayName("login 실패 - 유효하지 않은 parameter(id, password)")
    void loginTest_fail_invalidParameter_id_password() {
        // Given3
        final LoginRequest request = new LoginRequest("", "");

        // When3
        Set<ConstraintViolation<LoginRequest>> result = validator.validate(request);

        // Then3
        Assertions.assertThat(result.size()).isEqualTo(2);
        DtoValidationUtil.validateField(LoginRequest.class, result);
    }

}
