package com.zavan.dedesite.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Value("${app.upload.dir:/var/www/uploads}")
    private String uploadDir;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // Serve /uploads/** from the filesystem
    String location = "file:" + (uploadDir.endsWith("/") ? uploadDir : uploadDir + "/");
    registry.addResourceHandler("/uploads/**").addResourceLocations(location);
  }
}