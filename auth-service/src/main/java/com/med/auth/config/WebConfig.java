package com.med.auth.config;

import com.med.auth.interceptor.TokenInterceptor;
import com.med.auth.util.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private JwtUtil jwtUtil;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 手动new拦截器，把容器中的jwtUtil传进去
        registry.addInterceptor(new TokenInterceptor(jwtUtil))
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/login", "/auth/register", "/auth/logout");
    }
}