package com.med.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.med.auth.entity.SysUser;
import com.med.common.result.Result;
import java.util.List;

public interface SysUserService extends IService<SysUser> {
    void register(SysUser sysUser);
    String login(SysUser sysUser);
    SysUser getByUsername(String username);
    void updatePwd(Long userId, String oldPwd, String newPwd);
    Result<?> userPage(Long pageNum, Long pageSize, String keyword);
    SysUser getLoginUser();
    void changeStatus(Long userId, Integer status);

    // 新增接口方法
    List<SysUser> listAllUserSimple();
    Long countAllUser();
    Long countEnableUser();
    Long countDisableUser();
    void resetPwd(Long userId);
    void updateAvatar(Long userId, String avatarUrl);
}