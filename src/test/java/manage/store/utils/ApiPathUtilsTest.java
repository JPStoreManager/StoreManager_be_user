package manage.store.utils;

import manage.store.config.WebConfiguration;
import manage.store.consts.Tags;
import manage.store.exception.InvalidParameterException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag(Tags.Test.UNIT)
public class ApiPathUtilsTest {

    @Test
    @DisplayName("getPath 성공")
    void getPath_success() {
        // Given
        final ApiPathUtils.ApiName loginApiName = ApiPathUtils.ApiName.LOGIN;

        // When
        String path = ApiPathUtils.getPath(loginApiName);

        // Then
        Assertions.assertThat(path).isEqualTo(WebConfiguration.getContextPath() + "/login");
    }

    @Test
    @DisplayName("getPath 실패 - null parameter 입력")
    void getPath_fail_nullParameter() {
        // Given
        final ApiPathUtils.ApiName loginApiName = null;

        // When
        assertThrows(InvalidParameterException.class, () -> ApiPathUtils.getPath(loginApiName));
    }
}