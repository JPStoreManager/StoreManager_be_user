package manage.store.utils;

import manage.store.exception.InvalidParameterException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.Random;

public class SecretUtils {

    private static final BCryptPasswordEncoder encryptor = new BCryptPasswordEncoder();

    private static final String otpChar = "0123456789abcdefghijklmnopqrstuvwxyz";


    /**
     * 문자열를 암호화
     * @param str 암호화할 문자열
     * @return 암호화된 문자열 <br> 빈문자열이면 null 반환
     */
    public static String encrypt(String str) {
        if(!StringUtils.hasText(str)) return null;
        return encryptor.encode(str);
    }

    /**
     * raw 문자열과 암호화된 기준 문자열을 비교
     * @param rawStr 비밀번호
     * @param standardStr 암호화된 비밀번호
     * @return 비밀번호가 일치하면 true, 일치하지 않으면 false
     */
    public static Boolean verify(String rawStr, String standardStr) {
        return encryptor.matches(rawStr, standardStr);
    }

    /**
     * 길이에 맞는 OTP 생성 <br>
     * 영어 대소문자 및 숫자로 구성
     * @param length
     * @return
     */
    public static String createOtp(int length) {
        if(length <= 0) throw new InvalidParameterException("Length must be over 0");

        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int idx = random.nextInt(otpChar.length());
            otp.append(otpChar.charAt(idx));
        }

        return otp.toString();
    }

}