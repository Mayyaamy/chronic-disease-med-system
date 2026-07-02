package com.med.patient.feign;

import com.med.auth.entity.SysUser;
import com.med.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "auth-service")
public interface AuthFeignClient {
    @GetMapping("/{id}")
    Result<SysUser> getById(@PathVariable Long id);
}