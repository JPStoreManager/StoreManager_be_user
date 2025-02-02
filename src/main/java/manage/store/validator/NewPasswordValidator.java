package manage.store.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import manage.store.annotation.NewPassword;
import org.springframework.util.StringUtils;

public class NewPasswordValidator extends BaseValidator  implements ConstraintValidator<NewPassword, String> {

    // 영문자와 숫자 1개 이상을 포함
    private static final String VALID_REGEX = "^(?=.*[0-9])(?=.*[A-Za-z])[A-Za-z0-9]+$";

    private static final int MIN_LEN = 8;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isPwLengthOverMin = StringUtils.hasText(value) && value.length() >= MIN_LEN;
        if (!isPwLengthOverMin) {
            addValidateErrorMsg(context, "비밀번호는 최소 8자리 이상 입력해주세요.");
            return false;
        }

        boolean isPwContainAlphabet = value.matches(VALID_REGEX);
        if (!isPwContainAlphabet) {
            addValidateErrorMsg(context, "비밀번호 형식에 맞게 입력해주세요.");
            return false;
        }

        return true;
    }

    /**
     * 비밀번호 유효성 검사
     * @param newPassword 비밀번호
     * @return true - 유효한 비밀번호, false - 유효하지 않은 비밀번호
     */
    public static boolean isValid(String newPassword) {
        boolean isPwLengthOverMin = StringUtils.hasText(newPassword) && newPassword.length() >= MIN_LEN;
        if (!isPwLengthOverMin) {
            return false;
        }

        boolean isPwContainAlphabet = newPassword.matches(VALID_REGEX);
        if (!isPwContainAlphabet) {
            return false;
        }

        return true;
    }

}
