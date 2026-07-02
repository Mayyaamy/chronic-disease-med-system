package com.med.patient.controller;

import com.med.patient.feign.AuthFeignClient;
import com.med.auth.entity.SysUser;
import com.med.common.result.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feign/test")
public class FeignTestController {
    @Resource
    private AuthFeignClient authFeignClient;

    @GetMapping("/user/{uid}")
    public Result<SysUser> queryUser(@PathVariable Long uid){
        // 跨服务调用 auth-service 查询用户
        return authFeignClient.getById(uid);
    }
}