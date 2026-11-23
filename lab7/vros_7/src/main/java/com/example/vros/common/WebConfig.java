package com.example.vros.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public ShallowEtagHeaderFilter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }

    // MIGRARE API: pe fiecare raspuns din /api/v1/customers anuntam ca v1 e depreciata
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                response.setHeader("Deprecation", "true");
                response.setHeader("Sunset", "Wed, 30 Jun 2027 23:59:59 GMT");
                response.setHeader("Link", "</api/v2/customers>; rel=\"successor-version\"");
                return true; // continua spre controller
            }
        }).addPathPatterns("/api/v1/customers/**");
    }
}