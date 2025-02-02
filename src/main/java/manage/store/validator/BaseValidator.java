package manage.store.validator;

import jakarta.validation.ConstraintValidatorContext;

public class BaseValidator {

    /**
     * validation 실패 시 오류 메세지를 남길 수 있도록 하는 메소드
     *
     * @param msg 오류 발생에 대한 메세지
     * @param context 오류 메세지를 남길 수 있는 context
     */
    protected void addValidateErrorMsg(ConstraintValidatorContext context, String msg) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(msg)
                .addConstraintViolation();
    }

}
