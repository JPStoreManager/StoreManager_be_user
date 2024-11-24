package manage.store.testUtils;

import jakarta.validation.ConstraintViolation;
import org.assertj.core.api.Assertions;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service 계층의 DTO에 대한 Validation 테스트를 위한 유틸리티 클래스
 */
public class DtoValidationUtil {

    /**
     * 의도했던 유효하지 않은 field들이 실제 검증 결과와 동일한지 검증
     * @param invalidFields 유효하지 않은 field array
     * @param result 검증 결과
     * @param <T> DTO 타입
     */
    public static <T> void validateField(String[] invalidFields, Set<ConstraintViolation<T>> result) {
        Set<String> fields = Arrays.stream(invalidFields).collect(Collectors.toSet());
        Iterator<ConstraintViolation<T>> iterator = result.iterator(); // Iterator를 한 번만 생성
        while(iterator.hasNext()) {
            String field = iterator.next().getPropertyPath().toString();
            Assertions.assertThat(fields.contains(field)).isTrue();
        }
    }

    /**
     * DTO의 모든 field에 대한 검증 결과와 비교
     * @param clz DTO 클래스
     * @param result 검증 결과
     * @param <T> DTO 타입
     */
    public static <T> void validateField(Class<T> clz, Set<ConstraintViolation<T>> result) {
        String[] fields = Arrays.stream(clz.getDeclaredFields())
                .map(f -> f.getName())
                .toList().toArray(new String[]{});

        validateField(fields, result);
    }
}
