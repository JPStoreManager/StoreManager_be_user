package manage.store.service.mail;

import lombok.RequiredArgsConstructor;
import manage.store.exception.InvalidParameterException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${email.from}")
    private String systemEmail;

    @Value("${email.title-prefix}")
    private String mailTitlePrefix;

    private final JavaMailSender mailSender;

    /**
     * Otp 메일 전송
     * @param to 수신자 이메일
     * @param otp 전송할 otp
     * @throws IOException 메일 전송 과정에서 발생한 오류
     */
    public void sendOtpMail(String to, String otp) throws IOException {
        if(!StringUtils.hasText(to) || !StringUtils.hasText(otp)) throw new InvalidParameterException("to email or otp is empty");

        Path path = Path.of(new ClassPathResource("template/otp-mail-message.html").getURI());
        String msgTemplate = Files.readString(path);
        String msg = msgTemplate.replace("{otp}", otp);

        sendMail(to, "비밀번호 설정 OTP 발송", msg);
    }

    private void sendMail(String to, String subject, String msg) {
        MimeMessagePreparator preparator = (mimeMsg) -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, StandardCharsets.UTF_8.toString());

            helper.setFrom(systemEmail);
            helper.setTo(to);
            helper.setSubject(mailTitlePrefix + subject);
            helper.setText(msg, true);
        };

        mailSender.send(preparator);
    }

}
