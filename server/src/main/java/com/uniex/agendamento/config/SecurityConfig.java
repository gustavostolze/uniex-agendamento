package com.uniex.agendamento.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Libera o h2-console e todas as rotas da API sem autenticação
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll() // Passando a String direto
                        .anyRequest().permitAll()
                )
                // 2. Desativa a proteção CSRF (necessário para conseguir clicar nas tabelas do H2)
                .csrf(csrf -> csrf.disable())
                // 3. Permite o uso de frames (o painel esquerdo do H2-console usa frames)
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}
