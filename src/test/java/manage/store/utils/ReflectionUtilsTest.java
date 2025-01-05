package manage.store.utils;

import manage.store.consts.Tags;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag(Tags.Test.UNIT)
class ReflectionUtilsTest {

    /** getCurMethodName */
    @Test
    @DisplayName("getCurMethodName 성공")
    public void getCurMethodName_success() {
        // Given - When - Then
        Assertions.assertThat(ReflectionUtils.getCurMethodName()).isEqualTo("getCurMethodName_success");
    }

}