package com.med.auth.config;

import com.med.auth.interceptor.AuthInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Resource
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                // 放行登录、注册、Knife4j文档地址
                .excludePathPatterns("/user/login", "/user/register", "/doc.html", "/webjars/**", "/swagger-resources/**");
    }
}