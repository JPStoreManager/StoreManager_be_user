package manage.store.utils;

import manage.store.consts.Tags;
import manage.store.exception.InternalErrorException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag(Tags.Test.UNIT)
class ExceptionUtilsTest {

    /** getExceptionErrorMsg */
    @Test
    @DisplayName("getExceptionErrorMsg 성공")
    void getExceptionErrorMsg_success() {
        // Given
        class Inner1 {
            static void call() {
                Inner2.call();
            }
            private static class Inner2 {
                static void call() {
                    Inner3.error();
                }
                static class Inner3 {
                    static void error() {
                        throw new InternalErrorException("test exception");
                    }
                }
            }
        }

        // When
        try{
            Inner1.call();
        } catch (InternalErrorException e) {
            String exceptionErrorMsg = ExceptionUtils.getExceptionErrorMsg(e);
            assertThat(exceptionErrorMsg.contains("at manage.store.utils.ExceptionUtilsTest$1Inner1.call"));
            assertThat(exceptionErrorMsg.contains("at manage.store.utils.ExceptionUtilsTest$1Inner1$Inner2.call"));
            assertThat(exceptionErrorMsg.contains("at manage.store.utils.ExceptionUtilsTest$1Inner1$Inner2$Inner3.error"));
            assertThat(exceptionErrorMsg.contains("manage.store.exception.InternalErrorException: test exception"));
        }
    }
}