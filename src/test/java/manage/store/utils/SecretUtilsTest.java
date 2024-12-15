package manage.store.utils;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

}
