package xyz.lurkyphish2085.snipshare.snip.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${client.url}")
    private String clientURL;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(clientURL) // Adjust this to your frontend URL
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Adjust as needed
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
