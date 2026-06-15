package com.example.gad.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://127.0.0.1:5500", "http://localhost:5500"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Location")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/uploads/**")
                .addResourceLocations("file:uploads/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("file:static/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("forward:/login.html");
        registry.addViewController("/register").setViewName("forward:/register.html");
        registry.addViewController("/recuperar_senha").setViewName("forward:/recuperar_senha.html");
        registry.addViewController("/trocar_senha").setViewName("forward:/trocar_senha.html");
        registry.addViewController("/painel_cliente").setViewName("forward:/painel_cliente.html");
        registry.addViewController("/painel_redator").setViewName("forward:/painel_redator.html");
        registry.addViewController("/postagens_cliente").setViewName("forward:/postagens_cliente.html");
        registry.addViewController("/config").setViewName("forward:/config.html");
        registry.addViewController("/perfil_cliente").setViewName("forward:/perfil_cliente.html");
        registry.addViewController("/perfil_redator").setViewName("forward:/perfil_redator.html");
        registry.addViewController("/cliente_convite").setViewName("forward:/cliente_convite.html");
        registry.addViewController("/redator_convite").setViewName("forward:/redator_convite.html");
        registry.addViewController("/token_check").setViewName("forward:/token_check.html");
    }
}

