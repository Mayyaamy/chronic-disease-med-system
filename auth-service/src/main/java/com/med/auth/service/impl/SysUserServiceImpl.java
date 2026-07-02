package com.med.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.med.auth.entity.SysUser;
import com.med.auth.mapper.SysUserMapper;
import com.med.auth.service.SysUserService;
import com.med.auth.util.JwtUtil;
import com.med.auth.util.PasswordUtil;
import com.med.common.result.Result;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public void register(SysUser sysUser) {
        SysUser exist = getByUsername(sysUser.getUsername());
        if(exist != null){
            throw new RuntimeException("账号已存在");
        }
        sysUser.setPassword(PasswordUtil.encryptPwd(sysUser.getPassword()));
        sysUser.setStatus(1);
        save(sysUser);
    }

    @Override
    public String login(SysUser sysUser) {
        SysUser user = getByUsername(sysUser.getUsername());
        if(user == null){
            throw new RuntimeException("账号不存在");
        }
        if(user.getStatus() == 0){
            throw new RuntimeException("账号已被禁用");
        }
        if(!PasswordUtil.checkPwd(sysUser.getPassword(), user.getPassword())){
            throw new RuntimeException("密码错误");
        }
        return JwtUtil.generateToken(user.getId());
    }

    @Override
    public SysUser getByUsername(String username) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        return getOne(wrapper);
    }

    @Override
    public void updatePwd(Long userId, String oldPwd, String newPwd) {
        SysUser user = getById(userId);
        if(user == null) throw new RuntimeException("用户不存在");
        if(!PasswordUtil.checkPwd(oldPwd, user.getPassword())){
            throw new RuntimeException("原密码错误");
        }
        user.setPassword(PasswordUtil.encryptPwd(newPwd));
        updateById(user);
    }

    @Override
    public Result<?> userPage(Long pageNum, Long pageSize, String keyword) {
        IPage<SysUser> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if(keyword != null && !keyword.isEmpty()){
            wrapper.like(SysUser::getUsername, keyword).or().like(SysUser::getRealName, keyword);
        }
        page(page, wrapper);
        return Result.success(page);
    }

    @Override
    public SysUser getLoginUser() {
        SysUser user = JwtUtil.getLoginUser();
        if(user == null) throw new RuntimeException("未登录");
        return user;
    }

    @Override
    public void changeStatus(Long userId, Integer status) {
        SysUser user = getById(userId);
        if(user == null) throw new RuntimeException("用户不存在");
        user.setStatus(status);
        updateById(user);
    }

    // ===================== 新增方法实现 =====================
    @Override
    public List<SysUser> listAllUserSimple() {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(SysUser::getId, SysUser::getUsername, SysUser::getRealName);
        return list(wrapper);
    }

    @Override
    public Long countAllUser() {
        return count();
    }

    @Override
    public Long countEnableUser() {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getStatus,1);
        return count(wrapper);
    }

    @Override
    public Long countDisableUser() {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getStatus,0);
        return count(wrapper);
    }

    @Override
    public void resetPwd(Long userId) {
        SysUser user = getById(userId);
        if(user == null) throw new RuntimeException("用户不存在");
        user.setPassword(PasswordUtil.encryptPwd("123456"));
        updateById(user);
    }

    @Override
    public void updateAvatar(Long userId, String avatarUrl) {
        SysUser login = getLoginUser();
        if(!login.getId().equals(userId)){
            throw new RuntimeException("仅可修改自己头像");
        }
        SysUser user = new SysUser();
        user.setId(userId);
        user.setAvatar(avatarUrl);
        updateById(user);
    }
}