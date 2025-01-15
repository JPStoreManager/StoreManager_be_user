package manage.store.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import manage.store.annotation.UserId;
import org.springframework.util.StringUtils;

public class UserIdValidator extends BaseValidator implements ConstraintValidator<UserId, String> {

    private static final int MAX_LEN = 100;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isUserIdNotBlank = StringUtils.hasText(value);
        if(!isUserIdNotBlank) {
            addValidateErrorMsg(context, "아이디는 공란일 수 없습니다.");
            return false;
        }

        boolean isUserIdLengthUnderMax = value.length() <= MAX_LEN;
        if(!isUserIdLengthUnderMax) {
            addValidateErrorMsg(context, "아이디는 100자 이하로 입력해주세요.");
            return false;
        }

        return true;
    }

}
