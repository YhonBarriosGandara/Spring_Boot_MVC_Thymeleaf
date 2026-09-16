package com.udc.fincas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public WebMvcConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/dashboard", "/fincas/**", "/usuarios/**", "/reportes/**")
                .excludePathPatterns("/login", "/logout", "/recuperar-clave/**", "/error", "/static/**", "/css/**", "/js/**", "/images/**");
    }
}
