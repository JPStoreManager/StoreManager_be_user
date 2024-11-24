package manage.store.testUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Test에 사용되는 공통 유틸리티 클래스
 */
public interface CommonUtils {

    /**
     * 주어진 길이만큼의 랜덤 문자열 반환
     * @param length 문자열 길이
     * @return 랜덤 문자열
     */
     static String getRandomString(int length) {
        String randomString = "";
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < length; i++) {
            int index = (int) (characters.length() * Math.random());
            randomString += characters.charAt(index);
        }
        return randomString;
    }

    /**
     * 주어진 형식에 맞게 형식화된 Random 날짜 문자열 반환
     * @param format 형식
     * @return 형식화된 날짜 문자열
     */
    static String getRandomDate(String format) {
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        LocalDate endDate = LocalDate.now();

        long randomSec = ThreadLocalRandom.current().nextLong(startDate.toEpochDay(), endDate.toEpochDay());

        LocalDate randomDate = LocalDate.ofEpochDay(randomSec);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

        return formatter.format(randomDate);
    }

    /**
     * yyyy-MM-dd 형식의 랜덤 날짜 문자열 반환
     * @return yyyy-MM-dd 형식의 랜덤 날짜 문자열
     */
    static String getRandomDate() {
        return getRandomDate("yyyy-MM-dd");
    }

    /**
     * 1 ~ Integer.MAX_VALUE 사이의 랜덤 정수 반환
     * @return 랜덤 정수
     */
    static Integer getRandomInt() {
        return ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
    }

    /**
     * 1 ~ Long.MAX_VALUE 사이의 랜덤 Long 반환
     * @return 랜덤 Long
     */
    static Long getRandomLong() {
        return ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE);
    }


}
