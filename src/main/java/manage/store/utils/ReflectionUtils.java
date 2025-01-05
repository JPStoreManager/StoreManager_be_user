package manage.store.utils;

import manage.store.exception.InternalErrorException;

public class ReflectionUtils {

    /**
     * 현재 실행중인 함수의 이름을 반환 (이 함수를 호출한 함수의 이름 반환)
     * @return 이 함수의 이름
     * @throws InternalErrorException 현재 thread의 stack trace 길이가 2 이하일 때
     */
    public static String getCurMethodName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // [0] stack trace
        // [1] getCurMethodName
        // [2] 실제 실행 함수(getCurMethodName함수를 호출한 함수)
        if(stackTrace.length <= 2) throw new InternalErrorException("The length of Stack trace is less than 2");

        return stackTrace[2].getMethodName();
    }

}
