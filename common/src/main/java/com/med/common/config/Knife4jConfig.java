package com.med.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("慢病医疗分布式微服务系统-接口文档")
                        .version("1.0.0")
                        .description("课程设计 | 53+业务接口 | JWT鉴权 | Nacos注册配置中心 | Redis缓存"));
    }
}