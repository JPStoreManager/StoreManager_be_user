package manage.store.service.mail;

import manage.store.consts.Tags;
import manage.store.exception.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@Tag(Tags.Test.UNIT)
@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private MailService mailService;

    @Test
    @DisplayName("sendOtpMail 성공 (No exception)")
    void sendOtpMail_success() throws IOException {
        // Given
        final String email = "chickenman10@naver.com", otp = "123456";

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // When
        mailService.sendOtpMail(email, otp);

        // Then
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("sendOtpMail 실패 - 잘못된 파라미터")
    void sendOtpMail_fail_InvalidParameter() throws IOException {
        // Given
        final String email = "chickenman10@naver.com", otp = "123456";

        // When - Then
        assertThrows(InvalidParameterException.class, () -> mailService.sendOtpMail(email, null));
        assertThrows(InvalidParameterException.class, () -> mailService.sendOtpMail(" ", otp));
        assertThrows(InvalidParameterException.class, () -> mailService.sendOtpMail("", null));
    }

}