package com.zavan.dedesite.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // https://zavan.com.br/uploads/** -> filesystem: ./uploads/
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/var/www/uploads/") // <-- barra após file:
                .setCachePeriod(3600);
    }
}