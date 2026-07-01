package com.med.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.med.auth.entity.User;
import com.med.auth.exception.BusinessException;
import com.med.auth.mapper.UserMapper;
import com.med.auth.service.UserService;
import com.med.auth.util.PasswordUtil;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User login(String username, String password) {
        User user = lambdaQuery().eq(User::getUsername, username).one();
        if(user == null){
            throw new BusinessException("用户名不存在");
        }
        if(!PasswordUtil.matches(password, user.getPassword())){
            throw new BusinessException("密码不正确");
        }
        return user;
    }

    @Override
    public User getByUsername(String username) {
        return lambdaQuery().eq(User::getUsername, username).one();
    }

    @Override
    public boolean register(User user) {
        User existUser = getByUsername(user.getUsername());
        if(existUser != null){
            throw new BusinessException("该用户名已被注册");
        }
        // 注册密码加密入库
        String encryptPwd = PasswordUtil.encrypt(user.getPassword());
        user.setPassword(encryptPwd);
        return save(user);
    }

    @Override
    public User getById(Long userId) {
        return super.getById(userId);
    }

    @Override
    public boolean updateUserInfo(User user) {
        // 禁止修改用户名
        user.setUsername(null);
        return updateById(user);
    }

    @Override
    public boolean updatePassword(Long userId, String oldPwd, String newPwd) {
        User user = getById(userId);
        if(!PasswordUtil.matches(oldPwd, user.getPassword())){
            throw new BusinessException("原密码输入错误");
        }
        // 新密码加密更新
        user.setPassword(PasswordUtil.encrypt(newPwd));
        return updateById(user);
    }
}