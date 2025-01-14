package manage.store.utils;

import com.google.common.collect.ImmutableMap;
import manage.store.config.WebConfiguration;

public class ApiPathUtils {

    public enum ApiName {
        LOGIN("login"),
        FIND_PW_SEND_OTP("findPw_sendOtp"),
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
            .build();

    /**
     * apiName에 해당하는 path를 반환한다.
     * @param apiName path를 가져올 apiName
     * @return apiName에 해당하는 path
     * @throws RuntimeException apiName이 null인 경우
     */
    public static String getPath(ApiName apiName) {
        if(apiName == null) throw new IllegalArgumentException("apiName must not be null");

        return apiPathByName.get(apiName);
    }
}
