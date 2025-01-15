package manage.store.service.find;

import manage.store.DTO.common.BaseResponse;
import manage.store.DTO.entity.User;
import manage.store.consts.Message;
import manage.store.consts.SuccessFlag;
import manage.store.consts.Tags;
import manage.store.data.UserData;
import manage.store.exception.InvalidParameterException;
import manage.store.repository.UserAccountRepository;
import manage.store.service.mail.MailService;
import manage.store.utils.SecretUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;

@Tag(Tags.Test.UNIT)
@ExtendWith(MockitoExtension.class)
class FindUserServiceImplTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private MailService mailService;

    @InjectMocks
    private FindUserServiceImpl findUserService;


    /** sendOtp */
    @Test
    @DisplayName("sendOtp 성공")
    void sendOtp_success() throws IOException {
        // Given
        final String userId = "tester", email = "tester@gmail.com";

        final User user = UserData.user1;
        user.setId(userId);
        user.setEmail(email);
        final String otp = "otp123";

        given(userAccountRepository.selectUserById(userId)).willReturn(user);
        given(userAccountRepository.updateUser(user)).willReturn(1);
        doNothing().when(mailService).sendOtpMail(email, otp);

        try(MockedStatic<SecretUtils> SecretUtilsMock = mockStatic(SecretUtils.class)) {
            SecretUtilsMock.when(() -> SecretUtils.createOtp(6))
                    .thenReturn(otp);

            // When
            BaseResponse result = findUserService.sendOtp(userId, email);

            // Then
            assertThat(result.getResult()).isEqualTo(SuccessFlag.Y);
            assertThat(result.getMsg()).isEqualTo(Message.FIND_PW_SEND_OTP_SUCCESS);
        }
    }

    @Test
    @DisplayName("sendOtp 실패 - 잘못된 사용자 id / email")
    void sendOtp_fail_invalidUserIdOrEmail() {
        // Given
        final String userId = "tester", email = "tester@gmail.com";

        // When - Then
        assertThrows(InvalidParameterException.class, () -> findUserService.sendOtp(userId, null));
        assertThrows(InvalidParameterException.class, () -> findUserService.sendOtp(userId, ""));
        assertThrows(InvalidParameterException.class, () -> findUserService.sendOtp(userId, "  "));
        assertThrows(InvalidParameterException.class, () -> findUserService.sendOtp(null, email));
        assertThrows(InvalidParameterException.class, () -> findUserService.sendOtp("", email));
        assertThrows(InvalidParameterException.class, () -> findUserService.sendOtp(" ", email));
    }

    @Test
    @DisplayName("sendOtp 실패 - 존재하지 않는 사용자")
    void sendOtp_fail_notExistUser() {
        // Given
        final String userId = "tester", email = "tester@gmail.com";

        given(userAccountRepository.selectUserById(any())).willReturn(null);

        // When
        assertThrows(InvalidParameterException.class, () -> findUserService.sendOtp(userId, email));
    }

    @Test
    @DisplayName("sendOtp 실패 - 계정과 일치하지 않는 이메일")
    void sendOtp_fail_notMatchEmail() {
        // Given
        final String userId = "tester", email = "tester@gmail.com";
        User user = UserData.user1;
        user.setId(userId);
        user.setEmail(user.getEmail() + "notSame");

        given(userAccountRepository.selectUserById(any())).willReturn(user);

        // When
        assertThrows(InvalidParameterException.class, () -> findUserService.sendOtp(userId, email));
    }


    /** validateOtp */
    @Test
    @DisplayName("validateOtp 성공")
    void validateOtp_success() {
        // Given
        final String userId = "tester", email = "tester@gmail.com", otp = "otp123";
        final User user = UserData.user1;
        user.setId(userId);
        user.setEmail(email);
        user.setOtp(otp);
        
        given(userAccountRepository.selectUserById(userId)).willReturn(user);
        
        // When
        BaseResponse result = findUserService.validateOtp(userId, email, otp);
        
        // Then
        assertThat(result.getResult()).isEqualTo(SuccessFlag.Y);
        assertThat(result.getMsg()).isEqualTo(Message.FIND_PW_VALIDATE_OTP_SUCCESS);
    }

    @Test
    @DisplayName("validateOtp 실패 - 잘못된 사용자 id / email")
    void validateOtp_fail_invalidUserIdOrEmail() {
        // Given
        final String userId = "tester", email = "tester@gmail.com", otp = "otp123";

        // When - Then
        assertThrows(InvalidParameterException.class, () -> findUserService.validateOtp(userId, null, otp));
        assertThrows(InvalidParameterException.class, () -> findUserService.validateOtp(userId, "", otp));
        assertThrows(InvalidParameterException.class, () -> findUserService.validateOtp(userId, "  ", otp));
        assertThrows(InvalidParameterException.class, () -> findUserService.validateOtp(null, email, otp));
        assertThrows(InvalidParameterException.class, () -> findUserService.validateOtp("", email, otp));
        assertThrows(InvalidParameterException.class, () -> findUserService.validateOtp(" ", email, otp));
    }

    @Test
    @DisplayName("validateOtp 실패 - 존재하지 않는 사용자")
    void validateOtp_fail_notExistUser() {
        // Given
        final String userId = "tester", email = "tester@gmail.com", otp = "otp123";

        given(userAccountRepository.selectUserById(any())).willReturn(null);

        // When
        assertThrows(InvalidParameterException.class, () -> findUserService.validateOtp(userId, email, otp));
    }

    @Test
    @DisplayName("validateOtp 실패 - 계정과 일치하지 않는 이메일")
    void validateOtp_fail_notMatchEmail() {
        // Given
        final String userId = "tester", email = "tester@gmail.com", otp = "otp123";
        User user = UserData.user1;
        user.setId(userId);
        user.setEmail(user.getEmail() + "notSame");

        given(userAccountRepository.selectUserById(any())).willReturn(user);

        // When
        assertThrows(InvalidParameterException.class, () -> findUserService.validateOtp(userId, email, otp));
    }

    @Test
    @DisplayName("validateOtp 실패 - 유효하지 않는 OTP")
    void validateOtp_fail_invalidOtp() {
        // Given
        final String userId = "tester", email = "tester@gmail.com", otp = "otp123";
        final User user = UserData.user1;
        user.setId(userId);
        user.setEmail(email);
        user.setOtp(otp + "notSame");
        
        given(userAccountRepository.selectUserById(any())).willReturn(user);
        
        // When
        BaseResponse result = findUserService.validateOtp(userId, email, otp);
        
        // Then
        assertThat(result.getResult()).isEqualTo(SuccessFlag.N);
        assertThat(result.getMsg()).isEqualTo(Message.FIND_PW_VALIDATE_OTP_FAIL_NOT_VALID);
    }


    /** updatePassword */
    @Test
    @DisplayName("updatePassword 성공")
    void updatePassword_success() {
        // Given
        final String userId = "tester", userEmail = "tester@gmail.com", pw = "qwer1234";
        final User user = UserData.user1;
        user.setId(userId);
        user.setEmail(userEmail);

        given(userAccountRepository.selectUserById(any())).willReturn(user);
        given(userAccountRepository.updateUser(user)).willReturn(1);
        
        // When
        BaseResponse result = findUserService.updatePassword(userId, userEmail, pw);
        
        // Then
        assertThat(result.getResult()).isEqualTo(SuccessFlag.Y);
        assertThat(result.getMsg()).isEqualTo(Message.FIND_PW_UPDATE_PW_SUCCESS);
    }

    @Test
    @DisplayName("updatePassword 실패 - 잘못된 사용자 id / email")
    void updatePassword_fail_invalidUserIdOrEmail() {
        // Given
        final String userId = "tester", email = "tester@gmail.com", pwd = "qwer1234";

        // When - Then
        assertThrows(InvalidParameterException.class, () -> findUserService.updatePassword(userId, null, pwd));
        assertThrows(InvalidParameterException.class, () -> findUserService.updatePassword(userId, "", pwd));
        assertThrows(InvalidParameterException.class, () -> findUserService.updatePassword(userId, "  ", pwd));
        assertThrows(InvalidParameterException.class, () -> findUserService.updatePassword(null, email, pwd));
        assertThrows(InvalidParameterException.class, () -> findUserService.updatePassword("", email, pwd));
        assertThrows(InvalidParameterException.class, () -> findUserService.updatePassword(" ", email, pwd));
    }

    @Test
    @DisplayName("updatePassword 실패 - 존재하지 않는 사용자")
    void updatePassword_fail_notExistUser() {
        // Given
        final String userId = "tester", email = "tester@gmail.com", pwd = "qwer1234";

        given(userAccountRepository.selectUserById(any())).willReturn(null);

        // When
        assertThrows(InvalidParameterException.class, () -> findUserService.updatePassword(userId, email, pwd));

    }

    @Test
    @DisplayName("updatePassword 실패 - 계정과 일치하지 않는 이메일")
    void updatePassword_fail_notMatchEmail() {
        // Given
        final String userId = "tester", email = "tester@gmail.com", pwd = "qwer1234";
        User user = UserData.user1;
        user.setId(userId);
        user.setEmail(user.getEmail() + "notSame");

        given(userAccountRepository.selectUserById(any())).willReturn(user);

        // When - Then
        assertThrows(InvalidParameterException.class, () -> findUserService.updatePassword(userId, email, pwd));
    }

    @Test
    @DisplayName("updatePassword 실패 - 잘못된 비밀번호 형식")
    void updatePassword_fail_wrongPassword() {
        // Given
        final String userId = "tester", email = "tester@gmail.com";
        final String[] passwords = { "@", "12345678", "abcdefgh", "@@@@####"};
        User user = UserData.user1;
        user.setId(userId);
        user.setEmail(email);

        given(userAccountRepository.selectUserById(any())).willReturn(user);

        // When - Then
        for (int i = 0; i < passwords.length; i++) {
            BaseResponse baseResponse = findUserService.updatePassword(userId, email, passwords[i]);

            assertThat(baseResponse.getResult()).isEqualTo(SuccessFlag.N);
            assertThat(baseResponse.getMsg()).isEqualTo(Message.FIND_PW_UPDATE_PW_FAIL_INVALID_PW);
        }
    }
}