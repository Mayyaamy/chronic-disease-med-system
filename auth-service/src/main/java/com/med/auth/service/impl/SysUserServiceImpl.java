package com.med.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.med.auth.entity.SysUser;
import com.med.auth.mapper.SysUserMapper;
import com.med.auth.service.SysUserService;
import com.med.common.exception.BusinessException;
import com.med.common.result.Result;
import com.med.common.result.ResultCodeEnum;
import com.med.common.util.JwtUtil;
import com.med.common.util.MD5Util;
import com.med.common.util.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public Result<String> login(String username, String password) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        SysUser user = getOne(wrapper);
        if (user == null) {
            throw new BusinessException(ResultCodeEnum.PWD_ERROR);
        }
        if (!MD5Util.encrypt(password).equals(user.getPassword())) {
            throw new BusinessException(ResultCodeEnum.PWD_ERROR);
        }
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        // 生成Token
        String token = jwtUtil.generateToken(user.getId());
        // Redis缓存登录态，有效期24小时
        redisUtil.setExpire("login:user:" + user.getId(), token, 24, TimeUnit.HOURS);
        return Result.success(token);
    }

    @Override
    public Result<Void> register(SysUser user) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, user.getUsername());
        if (exists(wrapper)) {
            throw new BusinessException(ResultCodeEnum.USER_EXIST);
        }
        user.setPassword(MD5Util.encrypt(user.getPassword()));
        user.setStatus(1);
        save(user);
        return Result.success();
    }

    @Override
    public Result<SysUser> getCurrentUser(Long userId) {
        SysUser user = getById(userId);
        user.setPassword(null);
        return Result.success(user);
    }
}