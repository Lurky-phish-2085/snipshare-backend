package xyz.lurkyphish2085.snipshare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import xyz.lurkyphish2085.snipshare.auth.AuthenticationEntryPointImpl;
import xyz.lurkyphish2085.snipshare.auth.JwtAuthenticationFilter;
import xyz.lurkyphish2085.snipshare.auth.Roles;
import xyz.lurkyphish2085.snipshare.auth.UserDetailsServiceImpl;
import xyz.lurkyphish2085.snipshare.common.RestEndpoints;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPointImpl exceptionHandler;

    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService, JwtAuthenticationFilter authenticationFilter, AuthenticationEntryPointImpl exceptionHandler) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = authenticationFilter;
        this.exceptionHandler = exceptionHandler;
    }

    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf((csrf) -> csrf.disable())
            .sessionManagement((session) -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests((authorize) -> {
                authorize
                    .requestMatchers(HttpMethod.DELETE, RestEndpoints.SNIP + "/**")
                        .hasAnyRole(Roles.ADMIN.name(), Roles.USER.name())
                    .requestMatchers(RestEndpoints.AUTHENTICATION + "/**", RestEndpoints.SNIP + "/**")
                        .permitAll()
                    .anyRequest()
                        .authenticated();
            })
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling((handler) -> handler
                    .authenticationEntryPoint(exceptionHandler)
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
        return authConfig.getAuthenticationManager();
    }
}
