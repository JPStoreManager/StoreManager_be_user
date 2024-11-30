package manage.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // TODO 로그인 vs 비로그인 유저들에 대한 API 권한 설정 필요
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                // TODO 현재는 토큰이 없음으로 비활성화
                .csrf(csrf -> csrf.disable());
        return http.build();
    }

}
