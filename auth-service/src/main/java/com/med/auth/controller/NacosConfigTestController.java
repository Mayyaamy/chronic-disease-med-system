package com.med.auth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nacos/config")
@RefreshScope // 开启当前类配置动态刷新
public class NacosConfigTestController {

    @Value("${jwt.secret:默认密钥}")
    private String jwtSecret;

    @GetMapping("/get")
    public String getCurrentJwtSecret() {
        return "当前Nacos远端JWT密钥：" + jwtSecret;
    }
}