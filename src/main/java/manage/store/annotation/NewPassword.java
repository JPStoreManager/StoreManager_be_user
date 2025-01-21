package manage.store.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import manage.store.validator.NewPasswordValidator;

import java.lang.annotation.*;

/**
 * 비밀번호 유효성 검사 어노테이션 <br>
 * [조건] <br>
 * 1. 공란일 수 없음 <br>
 * 2. 영문자와 숫자를 각각 최소 1개 이상 보유해야 함 <br>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = NewPasswordValidator.class)
public @interface NewPassword {

    String message() default "비밀번호를 올바르게 입력해주세요.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default { };

}
