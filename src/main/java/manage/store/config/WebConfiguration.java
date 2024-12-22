package manage.store.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private static final String CONTEXT_PATH = "/user";

    public static String getContextPath() {
        return CONTEXT_PATH;
    }

    /**
     * context-path를 추가했음으로 정적 리소스 파일들에 대한 설정들 추가하기
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/docs/**")
                .addResourceLocations("classpath:/static/docs/");
    }

}
