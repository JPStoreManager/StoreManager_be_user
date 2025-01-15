package manage.store.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import manage.store.annotation.Password;
import org.springframework.util.StringUtils;

public class PasswordValidator extends BaseValidator  implements ConstraintValidator<Password, String> {

    // 영문자와 숫자 1개 이상을 포함
    private static final String VALID_REGEX = "^(?=.*[0-9])(?=.*[A-Za-z])[A-Za-z0-9]+$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isPwNotBlank = StringUtils.hasText(value);
        if (!isPwNotBlank) {
            addValidateErrorMsg(context, "비밀번호는 공란일 수 없습니다.");
            return false;
        }

        boolean isPwContainAlphabet = value.matches(VALID_REGEX);
        if (!isPwContainAlphabet) {
            addValidateErrorMsg(context, "비밀번호 형식에 맞게 입력해주세요.");
            return false;
        }

        return isPwNotBlank && isPwContainAlphabet;
    }

}
