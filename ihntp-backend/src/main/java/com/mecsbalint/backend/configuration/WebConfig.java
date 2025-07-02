package com.mecsbalint.backend.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${mecsbalint.app.file-upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String resolvedPath = Paths.get(uploadDir).toAbsolutePath().toUri().toString();

        registry
                .addResourceHandler("/api/images/**")
                .addResourceLocations(resolvedPath)
                .setCachePeriod(0)
                .setCacheControl(CacheControl.noCache());
    }
}
