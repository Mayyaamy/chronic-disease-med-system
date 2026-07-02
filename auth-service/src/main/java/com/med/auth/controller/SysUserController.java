package com.med.auth.controller;

import com.med.auth.entity.SysUser;
import com.med.auth.service.SysUserService;
import com.med.common.result.Result;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
public class SysUserController {

    private final SysUserService sysUserService;

    public SysUserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @PostMapping("/register")
    public Result<String> register(@RequestBody SysUser sysUser) {
        sysUserService.register(sysUser);
        return Result.success("注册成功");
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody SysUser sysUser) {
        String token = sysUserService.login(sysUser);
        return Result.success(token);
    }

    @GetMapping("/get/username")
    public Result<SysUser> getByUsername(@RequestParam(value = "username") String username) {
        return Result.success(sysUserService.getByUsername(username));
    }

    @PostMapping("/update/info")
    public Result<String> updateInfo(@RequestBody SysUser sysUser) {
        sysUserService.updateById(sysUser);
        return Result.success("信息修改成功");
    }

    @PostMapping("/update/pwd")
    public Result<String> updatePwd(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "oldPwd") String oldPwd,
            @RequestParam(value = "newPwd") String newPwd
    ) {
        sysUserService.updatePwd(userId, oldPwd, newPwd);
        return Result.success("密码修改成功");
    }

    @GetMapping("/page")
    public Result<?> page(
            @RequestParam(value = "pageNum") Long pageNum,
            @RequestParam(value = "pageSize") Long pageSize,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        return sysUserService.userPage(pageNum, pageSize, keyword);
    }

    @PutMapping("/status")
    public Result<String> changeStatus(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "status") Integer status
    ) {
        sysUserService.changeStatus(userId, status);
        return Result.success("状态修改成功");
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable(value = "id") Long id) {
        sysUserService.removeById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/current")
    public Result<SysUser> getCurrentUser() {
        return Result.success(sysUserService.getLoginUser());
    }

    // ========== 新增6个接口 ==========
    @GetMapping("/list/all")
    public Result<List<SysUser>> listAllUserSimple() {
        return Result.success(sysUserService.listAllUserSimple());
    }

    @GetMapping("/count/all")
    public Result<Long> countAllUser() {
        return Result.success(sysUserService.countAllUser());
    }

    @GetMapping("/count/enable")
    public Result<Long> countEnableUser() {
        return Result.success(sysUserService.countEnableUser());
    }

    @GetMapping("/count/disable")
    public Result<Long> countDisableUser() {
        return Result.success(sysUserService.countDisableUser());
    }

    @PostMapping("/reset/pwd")
    public Result<String> resetPwd(@RequestParam(value = "userId") Long userId) {
        sysUserService.resetPwd(userId);
        return Result.success("密码重置为123456成功");
    }

    @PutMapping("/avatar")
    public Result<String> updateAvatar(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "avatarUrl") String avatarUrl
    ) {
        sysUserService.updateAvatar(userId, avatarUrl);
        return Result.success("头像修改成功");
    }
}