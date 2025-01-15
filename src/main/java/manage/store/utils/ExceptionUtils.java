package manage.store.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {

    /**
     * 예외 원인 메시지를 문자열로 반환
     */
    public static String getExceptionErrorMsg(Throwable e) {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);

        return writer.toString();
    }

}
