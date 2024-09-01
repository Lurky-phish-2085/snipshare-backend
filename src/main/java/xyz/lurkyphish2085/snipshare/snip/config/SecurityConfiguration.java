package xyz.lurkyphish2085.snipshare.snip.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .httpBasic(withDefaults())
                .authorizeHttpRequests((authorize) -> { authorize
                        .requestMatchers("/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated();
                });

        return http.build();
    }
}
