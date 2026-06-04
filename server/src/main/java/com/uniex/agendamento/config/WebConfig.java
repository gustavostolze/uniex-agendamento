package com.uniex.agendamento.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica o CORS para todas as rotas da API
                .allowedOrigins(allowedOrigins) // Permite apenas a URL do seu React
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH") // Libera os métodos HTTP
                .allowedHeaders("*") // Aceita qualquer cabeçalho (Content-Type, Authorization, etc)
                .allowCredentials(true); // Permite o envio de cookies/credentials (fundamental para o OAuth2 depois!)
    }
}