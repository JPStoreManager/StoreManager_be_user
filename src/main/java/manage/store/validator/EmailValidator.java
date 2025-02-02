package manage.store.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import manage.store.annotation.UserEmail;
import org.springframework.util.StringUtils;

public class EmailValidator extends BaseValidator implements ConstraintValidator<UserEmail, String>{

    private static final String emailRegex = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isEmailNotBlank = StringUtils.hasText(value);
        if(!isEmailNotBlank) {
            addValidateErrorMsg(context, "이메일은 공란일 수 없습니다.");
            return false;
        }

        boolean isEmailMatchFormat = value.matches(emailRegex);
        if(!isEmailMatchFormat) {
            addValidateErrorMsg(context, "이메일 형식에 맞게 입력해주세요.");
            return false;
        }

        return true;
    }

}
