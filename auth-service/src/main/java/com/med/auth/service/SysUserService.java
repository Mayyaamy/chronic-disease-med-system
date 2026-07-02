package com.med.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.med.auth.entity.SysUser;
import com.med.auth.vo.LoginVO;
import com.med.common.result.Result;

public interface SysUserService extends IService<SysUser> {
    Result<String> login(String username, String password);
    Result<Void> register(SysUser user);
    Result<SysUser> getCurrentUser(Long userId);
}