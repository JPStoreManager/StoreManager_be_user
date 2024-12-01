package manage.store.config;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {

    private static final String BASE_PATH = "/user";

    /**
     * 컨트롤러 basic path로 user를 설정
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(BASE_PATH, c -> true);
    }

}
