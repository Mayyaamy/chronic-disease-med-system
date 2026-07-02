package com.med.auth.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.med.auth.entity.SysUser;
import com.med.auth.service.SysUserService;
import com.med.common.result.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class SysUserController {
    @Resource
    private SysUserService userService;

    // 1.登录
    @PostMapping("/login")
    public Result<String> login(@RequestParam String username, @RequestParam String password) {
        return userService.login(username, password);
    }
    // 2.注册
    @PostMapping("/register")
    public Result<Void> register(@RequestBody SysUser user) {
        return userService.register(user);
    }
    // 3.获取当前登录用户
    @GetMapping("/current")
    public Result<SysUser> getCurrent(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return userService.getCurrentUser(userId);
    }
    // 4.分页查询用户
    @GetMapping("/page")
    public Result<IPage<SysUser>> page(@RequestParam(defaultValue = "1") Long pageNum,
                                       @RequestParam(defaultValue = "10") Long pageSize,
                                       @RequestParam(required = false) String keyword) {
        return Result.success(userService.page(new Page<>(pageNum, pageSize), null));
    }
    // 5.根据ID查询
    @GetMapping("/{id}")
    public Result<SysUser> getById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }
    // 6.新增用户
    @PostMapping
    public Result<Void> add(@RequestBody SysUser user) {
        user.setPassword(user.getPassword() == null ? null : MD5Util.encrypt(user.getPassword()));
        userService.save(user);
        return Result.success();
    }
    // 7.修改用户
    @PutMapping
    public Result<Void> edit(@RequestBody SysUser user) {
        user.setPassword(null);
        userService.updateById(user);
        return Result.success();
    }
    // 8.删除单个
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.removeById(id);
        return Result.success();
    }
    // 9.批量删除
    @DeleteMapping("/batch")
    public Result<Void> batchDelete(@RequestBody java.util.List<Long> ids) {
        userService.removeByIds(ids);
        return Result.success();
    }
    // 10.修改密码
    @PutMapping("/pwd")
    public Result<Void> updatePwd(HttpServletRequest request,
                                  @RequestParam String oldPwd,
                                  @RequestParam String newPwd) {
        Long uid = (Long) request.getAttribute("currentUserId");
        SysUser user = userService.getById(uid);
        if (!MD5Util.encrypt(oldPwd).equals(user.getPassword())) {
            return Result.fail("原密码错误");
        }
        user.setPassword(MD5Util.encrypt(newPwd));
        userService.updateById(user);
        return Result.success();
    }
    // 11.修改头像
    @PutMapping("/avatar")
    public Result<Void> updateAvatar(HttpServletRequest request, @RequestParam String avatar) {
        Long uid = (Long) request.getAttribute("currentUserId");
        SysUser u = new SysUser();
        u.setId(uid);
        u.setAvatar(avatar);
        userService.updateById(u);
        return Result.success();
    }
    // 12.启用账号
    @PutMapping("/enable/{id}")
    public Result<Void> enable(@PathVariable Long id) {
        SysUser u = new SysUser();
        u.setId(id);
        u.setStatus(1);
        userService.updateById(u);
        return Result.success();
    }
    // 13.禁用账号
    @PutMapping("/disable/{id}")
    public Result<Void> disable(@PathVariable Long id) {
        SysUser u = new SysUser();
        u.setId(id);
        u.setStatus(0);
        userService.updateById(u);
        return Result.success();
    }
    // 14.退出登录（清理Redis）
    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        redisUtil.delete("login:user:" + uid);
        return Result.success();
    }
    // 15.统计总用户数
    @GetMapping("/count")
    public Result<Long> count() {
        return Result.success(userService.count());
    }
}