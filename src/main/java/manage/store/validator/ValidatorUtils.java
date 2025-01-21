package manage.store.validator;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class ValidatorUtils {

    /**
     * 기본 설정으로 초기화된 Validator를 반환
     * @return
     */
    public static Validator getDefaultValidator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }
}
