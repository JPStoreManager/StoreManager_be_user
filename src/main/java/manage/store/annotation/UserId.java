package manage.store.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import manage.store.validator.UserIdValidator;

import java.lang.annotation.*;

/**
 * 사용자 아이디 유효성 검사 어노테이션 <br>
 * [조건] <br>
 * 1. 공란일 수 없음 <br>
 * 2. 한자리 이상 100자리 이하 <br>
 */
@Documented
@Target(value= ElementType.FIELD)
@Retention(value= RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserIdValidator.class)
public @interface UserId {

    String message() default "아이디를 올바르게 입력해주세요.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default { };

}
