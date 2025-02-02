package manage.store.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import manage.store.validator.EmailValidator;

import java.lang.annotation.*;

/**
 * 사용자 이메일 유효성 검사 어노테이션 <br>
 * [조건] <br>
 * 1. 공란일 수 없음 <br>
 * 2. 이메일 형식 <br>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = EmailValidator.class)
public @interface UserEmail {

    String message() default "이메일을 올바르게 입력해주세요.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default { };
}
