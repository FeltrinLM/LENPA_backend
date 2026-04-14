package com.example.lenpa_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Diz para o Spring: "Quando o Angular pedir uma URL que comece com /uploads/,
        // procure na pasta física 'uploads' que está na raiz do projeto Java"
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}