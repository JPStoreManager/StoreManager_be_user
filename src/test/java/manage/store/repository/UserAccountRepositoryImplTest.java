package manage.store.repository;

import manage.store.DTO.entity.User;
import manage.store.data.UserData;
import manage.store.exception.InvalidParameterException;
import manage.store.repository.mapper.UserAccountMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserAccountRepositoryImplTest {

    @Mock
    private UserAccountMapper userAccountMapper;

    @InjectMocks
    private UserAccountRepositoryImpl userAccountRepository;

    /**
     * selectUserById
     */
    @Test
    @Disabled("select에서는 별다른 validation이 없음으로 테스트 생략")
    void selectUserById() {
    }

    /**
     * insertUser
     */
    @Test
    @DisplayName("insertUser 성공 - 사용자 정보 등록 성공")
    void insertUser_success() {
        // Given
        final User user = UserData.user1();
        given(userAccountMapper.insertUser(user)).willReturn(1);

        // When
        int updatedCnt = userAccountRepository.insertUser(user);

        // Then
        assertThat(updatedCnt).isEqualTo(1);
    }

    @Test
    @DisplayName("insertUser 실패 - 잘못된 파라미터")
    void insertUser_fail_invalidParameter() {
        // Given
        final User user = UserData.user1();

        // When - Then
        assertThrows(InvalidParameterException.class, () -> userAccountRepository.insertUser(null));

        user.setId(null);
        assertThrows(InvalidParameterException.class, () -> userAccountRepository.insertUser(user));
    }

    @Test
    @DisplayName("insertUser 실패 - 중복키 등의 DB 익셉션으로 인한 사용자 정보 등록 실패")
    void insertUser_fail_dbException() {
        // Given - When - Then1
        final User user = UserData.user1();
        given(userAccountMapper.insertUser(user)).willThrow(new DataIntegrityViolationException("DB Error"));
        int updatedCnt = userAccountRepository.insertUser(user);
        assertThat(updatedCnt).isEqualTo(0);

        // Given - When - Then2
        final User user2 = UserData.user2();
        given(userAccountMapper.insertUser(user2)).willThrow(new DuplicateKeyException("DB Error"));
        int updatedCnt2 = userAccountRepository.insertUser(user2);
        assertThat(updatedCnt2).isEqualTo(0);
    }

    /**
     * updateUser
     */
    @Test
    @DisplayName("updateUser 성공 - 사용자 정보 등록 성공")
    void updateUser_success() {
        // Given
        final User user = UserData.user1();
        given(userAccountMapper.updateUser(user)).willReturn(1);

        // When
        int updatedCnt = userAccountRepository.updateUser(user);

        // Then
        assertThat(updatedCnt).isEqualTo(1);
    }

    @Test
    @DisplayName("updateUser 실패 - 잘못된 파라미터")
    void updateUser_fail_invalidParameter() {
        // Given
        final User user = UserData.user1();

        // When - Then
        assertThrows(InvalidParameterException.class, () -> userAccountRepository.updateUser(null));

        user.setId(null);
        assertThrows(InvalidParameterException.class, () -> userAccountRepository.updateUser(user));
    }

    @Test
    @DisplayName("updateUser 실패 - 중복키 등의 DB 익셉션으로 인한 사용자 정보 등록 실패")
    void updateUser_fail_dbException() {
        // Given - When - Then1
        final User user = UserData.user1();
        given(userAccountMapper.updateUser(user)).willThrow(new DataIntegrityViolationException("DB Error"));
        int updatedCnt = userAccountRepository.updateUser(user);
        assertThat(updatedCnt).isEqualTo(0);

        // Given - When - Then2
        final User user2 = UserData.user2();
        given(userAccountMapper.updateUser(user2)).willThrow(new DuplicateKeyException("DB Error"));
        int updatedCnt2 = userAccountRepository.updateUser(user2);
        assertThat(updatedCnt2).isEqualTo(0);
    }
}