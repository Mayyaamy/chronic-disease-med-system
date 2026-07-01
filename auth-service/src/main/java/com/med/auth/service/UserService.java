package com.med.auth.service;

import com.med.auth.entity.User;

public interface UserService {
    // 登录校验
    User login(String username, String password);

    // 新增注册方法
    boolean register(User user);

    // 根据用户名查用户（判断账号是否已存在）
    User getByUsername(String username);

    // 根据ID查询用户
    User getById(Long userId);

    // 更新用户信息
    boolean updateUserInfo(User user);

    // 修改密码
    boolean updatePassword(Long userId, String oldPwd, String newPwd);
}