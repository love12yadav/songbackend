package com.example.song;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/songs/**")
                .allowedOrigins("http://localhost:3000", "https://justlisten-pi.vercel.app")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // <== THIS IS CRITICAL
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
