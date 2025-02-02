package manage.store.utils;

import com.google.common.collect.ImmutableMap;
import manage.store.config.WebConfiguration;
import manage.store.exception.InvalidParameterException;

public class ApiPathUtils {

    public enum ApiName {
        LOGIN("login"),
        FIND_PW_SEND_OTP("findPw_sendOtp"),
        FIND_PW_VALIDATE_OTP("findPw_validateOtp"),
        FIND_PW_UPDATE_PW("findPw_updatePw")
        ;

        private String name;

        ApiName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static boolean isApiName(String name) {
            for (ApiName value : ApiName.values()) {
                if(name.equals(value.getName())) {
                    return true;
                }
            }
            return false;
        }
    }

    private static final ImmutableMap<ApiName, String> apiPathByName = ImmutableMap.<ApiName, String>builder()
            .put(ApiName.LOGIN, WebConfiguration.getContextPath() + "/login")
            .put(ApiName.FIND_PW_SEND_OTP, WebConfiguration.getContextPath() + "/find/pw/sendOtp")
            .put(ApiName.FIND_PW_VALIDATE_OTP, WebConfiguration.getContextPath() + "/find/pw/validateOtp")
            .put(ApiName.FIND_PW_UPDATE_PW, WebConfiguration.getContextPath() + "/find/pw/updatePw")
            .build();

    /**
     * apiName에 해당하는 path를 반환한다.
     * @param apiName path를 가져올 apiName
     * @return apiName에 해당하는 path
     * @throws RuntimeException apiName이 null인 경우
     */
    public static String getPath(ApiName apiName) {
        if(apiName == null) throw new InvalidParameterException("apiName must not be null");

        return apiPathByName.get(apiName);
    }
}
