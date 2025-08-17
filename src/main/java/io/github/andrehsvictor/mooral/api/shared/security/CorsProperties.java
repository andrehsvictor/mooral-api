package io.github.andrehsvictor.mooral.api.shared.security;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("io.github.andrehsvictor.mooral-api.cors")
public class CorsProperties {

    private String allowedOrigins = "*";
    private String allowedMethods = "GET,POST,PUT,DELETE,PATCH,HEAD,OPTIONS";
    private String allowedHeaders = "Authorization,Content-Type";
    private Boolean allowCredentials = true;
    private long maxAge = 3600;

    public List<String> getAllowedOriginsList() {
        return List.of(allowedOrigins.split(","));
    }

    public List<String> getAllowedMethodsList() {
        return List.of(allowedMethods.split(","));
    }

    public List<String> getAllowedHeadersList() {
        return List.of(allowedHeaders.split(","));
    }

}