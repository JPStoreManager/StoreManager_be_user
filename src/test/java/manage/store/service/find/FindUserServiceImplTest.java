package manage.store.service.find;

import manage.store.DTO.common.BaseResponse;
import manage.store.DTO.entity.User;
import manage.store.DTO.find.FindPwSendOtpRequest;
import manage.store.DTO.find.FindPwUpdatePwRequest;
import manage.store.DTO.find.FindPwValidateOtpRequest;
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

        final User user = UserData.user1();
        user.setId(userId);
        user.setEmail(email);
        final String otp = "otp123";

        final FindPwSendOtpRequest request = new FindPwSendOtpRequest();
        request.setUserId(userId);
        request.setEmail(email);

        given(userAccountRepository.selectUserById(userId)).willReturn(user);
        given(userAccountRepository.updateUser(user)).willReturn(1);
        doNothing().when(mailService).sendOtpMail(email, otp);

        try(MockedStatic<SecretUtils> SecretUtilsMock = mockStatic(SecretUtils.class)) {
            SecretUtilsMock.when(() -> SecretUtils.createOtp(6))
                    .thenReturn(otp);

            // When
            BaseResponse result = findUserService.sendOtp(request);

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
        final String[][] params = {
                {null, null},
                {userId, null},
                {userId, ""},
                {userId, " "},

                {null, email},
                {"", email},
                {" ", email},

                {userId, "wrongEmail"},
                {userId, "wrongEmail@"},
                {userId, "wrongEmail.com"},
                {userId, "wrongEmail@.com"},
                {userId, "@com"},
        };

        // When - Then
        for (String[] param : params) {
            final FindPwSendOtpRequest request = new FindPwSendOtpRequest();
            request.setUserId(param[0]);
            request.setEmail(param[1]);

            assertThrows(InvalidParameterException.class, () -> findUserService.sendOtp(request));
        }
    }

    @Test
    @DisplayName("sendOtp 실패 - 존재하지 않는 사용자")
    void sendOtp_fail_notExistUser() {
        // Given
        final String userId = "tester", email = "tester@gmail.com";
        final FindPwSendOtpRequest request = new FindPwSendOtpRequest();
        request.setUserId(userId);
        request.setEmail(email);

        given(userAccountRepository.selectUserById(any())).willReturn(null);

        // When
        assertThrows(InvalidParameterException.class, () -> findUserService.sendOtp(request));
    }

    @Test
    @DisplayName("sendOtp 실패 - 계정과 일치하지 않는 이메일")
    void sendOtp_fail_notMatchEmail() {
        // Given
        final String userId = "tester", email = "tester@gmail.com";
        User user = UserData.user1();
        user.setId(userId);
        user.setEmail(user.getEmail() + "notSame");

       final FindPwSendOtpRequest request = new FindPwSendOtpRequest();
        request.setUserId(userId);
        request.setEmail(email);

        given(userAccountRepository.selectUserById(any())).willReturn(user);

        // When
        assertThrows(InvalidParameterException.class, () -> findUserService.sendOtp(request));
    }


    /** validateOtp */
    @Test
    @DisplayName("validateOtp 성공")
    void validateOtp_success() {
        // Given
        final String userId = "tester", email = "tester@gmail.com", otp = "otp123";
        final User user = UserData.user1();
        user.setId(userId);
        user.setEmail(email);
        user.setOtp(otp);

        final FindPwValidateOtpRequest request = new FindPwValidateOtpRequest();
        request.setUserId(userId);
        request.setEmail(email);
        request.setOtp(otp);

        given(userAccountRepository.selectUserById(userId)).willReturn(user);

        // When
        BaseResponse result = findUserService.validateOtp(request);
        
        // Then
        assertThat(result.getResult()).isEqualTo(SuccessFlag.Y);
        assertThat(result.getMsg()).isEqualTo(Message.FIND_PW_VALIDATE_OTP_SUCCESS);
    }

    @Test
    @DisplayName("validateOtp 실패 - 잘못된 사용자 id / email")
    void validateOtp_fail_invalidUserIdOrEmail() {
        // Given
        final String userId = "tester", email = "tester@gmail.com", otp = "otp123";
        final String[][] params = {
                {null, null, otp},
                {userId, null, otp},
                {userId, "", otp},
                {userId, " ", otp},

                {null, email, otp},
                {"", email, otp},
                {" ", email, otp},

                {userId, "wrongEmail", otp},
                {userId, "wrongEmail@", otp},
                {userId, "wrongEmail.com", otp},
                {userId, "wrongEmail@.com", otp},
                {userId, "@com", otp},
        };

        // When - Then
        for (String[] param : params) {
            final FindPwValidateOtpRequest request = new FindPwValidateOtpRequest();
            request.setUserId(param[0]);
            request.setEmail(param[1]);
            request.setOtp(param[2]);

            assertThrows(InvalidParameterException.class, () -> findUserService.validateOtp(request));
        }
    }

    @Test
    @DisplayName("validateOtp 실패 - 존재하지 않는 사용자")
    void validateOtp_fail_notExistUser() {
        // Given
        final String userId = "tester", email = "tester@gmail.com", otp = "otp123";
        final FindPwValidateOtpRequest request = new FindPwValidateOtpRequest();
        request.setUserId(userId);
        request.setEmail(email);
        request.setOtp(otp);

        given(userAccountRepository.selectUserById(any())).willReturn(null);

        // When
        assertThrows(InvalidParameterException.class, () -> findUserService.validateOtp(request));
    }

    @Test
    @DisplayName("validateOtp 실패 - 계정과 일치하지 않는 이메일")
    void validateOtp_fail_notMatchEmail() {
        // Given
        final String userId = "tester", email = "tester@gmail.com", otp = "otp123";
        User user = UserData.user1();
        user.setId(userId);
        user.setEmail(user.getEmail() + "notSame");

        final FindPwValidateOtpRequest request = new FindPwValidateOtpRequest();
        request.setUserId(userId);
        request.setEmail(email);
        request.setOtp(otp);

        // When
        assertThrows(InvalidParameterException.class, () -> findUserService.validateOtp(request));
    }

    @Test
    @DisplayName("validateOtp 실패 - 유효하지 않는 OTP")
    void validateOtp_fail_invalidOtp() {
        // Given
        final String userId = "tester", email = "tester@gmail.com", otp = "otp123";
        final User user = UserData.user1();
        user.setId(userId);
        user.setEmail(email);
        user.setOtp(otp + "notSame");

        final FindPwValidateOtpRequest request = new FindPwValidateOtpRequest();
        request.setUserId(userId);
        request.setEmail(email);
        request.setOtp(otp);

        given(userAccountRepository.selectUserById(any())).willReturn(user);

        // When
        BaseResponse result = findUserService.validateOtp(request);
        
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
        final User user = UserData.user1();
        user.setId(userId);
        user.setEmail(userEmail);

        final FindPwUpdatePwRequest request = new FindPwUpdatePwRequest();
        request.setUserId(userId);
        request.setEmail(userEmail);
        request.setNewPassword(pw);

        given(userAccountRepository.selectUserById(any())).willReturn(user);
        given(userAccountRepository.updateUser(user)).willReturn(1);

        // When
        BaseResponse result = findUserService.updatePassword(request);
        
        // Then
        assertThat(result.getResult()).isEqualTo(SuccessFlag.Y);
        assertThat(result.getMsg()).isEqualTo(Message.FIND_PW_UPDATE_PW_SUCCESS);
    }

    @Test
    @DisplayName("updatePassword 실패 - 잘못된 사용자 id / email")
    void updatePassword_fail_invalidUserIdOrEmail() {
        // Given
        final String userId = "tester", email = "tester@gmail.com", pwd = "qwer1234";
        final String[][] params = {
                {null, null, pwd},
                {userId, null, pwd},
                {userId, "", pwd},
                {userId, " ", pwd},

                {null, email, pwd},
                {"", email, pwd},
                {" ", email, pwd},

                {userId, "wrongEmail", pwd},
                {userId, "wrongEmail@", pwd},
                {userId, "wrongEmail.com", pwd},
                {userId, "wrongEmail@.com", pwd},
                {userId, "@com", pwd},
        };

        // When - Then
        for (String[] param : params) {
            final FindPwUpdatePwRequest request = new FindPwUpdatePwRequest();
            request.setUserId(param[0]);
            request.setEmail(param[1]);
            request.setNewPassword(param[2]);

            assertThrows(InvalidParameterException.class, () -> findUserService.updatePassword(request));
        }
    }

    @Test
    @DisplayName("updatePassword 실패 - 존재하지 않는 사용자")
    void updatePassword_fail_notExistUser() {
        // Given
        final String userId = "tester", email = "tester@gmail.com", pwd = "qwer1234";
        final FindPwUpdatePwRequest request = new FindPwUpdatePwRequest();
        request.setUserId(userId);
        request.setEmail(email);
        request.setNewPassword(pwd);

        given(userAccountRepository.selectUserById(any())).willReturn(null);

        // When
        assertThrows(InvalidParameterException.class, () -> findUserService.updatePassword(request));

    }

    @Test
    @DisplayName("updatePassword 실패 - 계정과 일치하지 않는 이메일")
    void updatePassword_fail_notMatchEmail() {
        // Given
        final String userId = "tester", email = "tester@gmail.com", pwd = "qwer1234";
        final User user = UserData.user1();
        user.setId(userId);
        user.setEmail(user.getEmail() + "notSame");

        final FindPwUpdatePwRequest request = new FindPwUpdatePwRequest();
        request.setUserId(userId);
        request.setEmail(email);
        request.setNewPassword(pwd);

        given(userAccountRepository.selectUserById(any())).willReturn(user);

        // When - Then
        assertThrows(InvalidParameterException.class, () -> findUserService.updatePassword(request));
    }

    @Test
    @DisplayName("updatePassword 실패 - 잘못된 비밀번호 형식")
    void updatePassword_fail_wrongPassword() {
        // Given
        final String userId = "tester", email = "tester@gmail.com";
        final String[] passwords = { "@", "12345678", "abcdefgh", "@@@@####"};
        final User user = UserData.user1();
        user.setId(userId);
        user.setEmail(email);

        given(userAccountRepository.selectUserById(any())).willReturn(user);

        // When - Then
        for (int i = 0; i < passwords.length; i++) {
            FindPwUpdatePwRequest request = new FindPwUpdatePwRequest();
            request.setUserId(userId);
            request.setEmail(email);
            request.setNewPassword(passwords[i]);

            assertThrows(InvalidParameterException.class, () -> findUserService.updatePassword(request));
        }
    }
}