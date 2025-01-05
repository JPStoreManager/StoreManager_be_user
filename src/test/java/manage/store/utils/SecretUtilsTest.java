package manage.store.utils;


import manage.store.consts.Tags;
import manage.store.exception.InvalidParameterException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag(Tags.Test.UNIT)
public class SecretUtilsTest {

    private static final String originStr = "password1234@!#";

    private static final String encryptedStr = "$2a$16$6bPTpGzroXvAA4R60RyrauS/6azbmxw9IALOv9TOSXwjK2bXO07G2";

    /** verify */
    @Test
    @DisplayName("verify 성공")
    public void verifyTest_success() {
        Assertions.assertThat(SecretUtils.verify(originStr, encryptedStr)).isTrue();
    }

    @Test
    @DisplayName("verify 실패 - 불일치 parameter 입력")
    public void verifyTest_fail_wrongRawData() {
        Assertions.assertThat(SecretUtils.verify("wrongPw", encryptedStr)).isFalse();
    }

    /** encrypt */
    @Test
    @DisplayName("encrypt 성공")
    public void encryptTest_success() {
        Assertions.assertThat(SecretUtils.encrypt(originStr)).isNotNull();
    }

    @Test
    @DisplayName("encrypt 실패 - 빈 문자열")
    public void encryptTest_fail_emptyString() {
        Assertions.assertThat(SecretUtils.encrypt("")).isNull();
    }

    /** createOtp */
    @Test
    @DisplayName("createOtp 성공")
    public void createOtp_success() {
        // Given
        int length = 6;

        // When - Then
        for (int i = 0; i < 5; i++) {
            String otp = SecretUtils.createOtp(length);
            Assertions.assertThat(otp.length()).isSameAs(length);
        }
    }

    @Test
    @DisplayName("createOtp 실패 - 0 이하의 length")
    public void createOtp_fail_lengthUnderOne() {
        // Given1
        int length1 = 0;

        // When1 - Then1
        assertThrows(InvalidParameterException.class, () -> SecretUtils.createOtp(length1));

        // Given2
        int length2 = -1;

        // When2 - Then2
        assertThrows(InvalidParameterException.class, () -> SecretUtils.createOtp(length2));
    }

}
