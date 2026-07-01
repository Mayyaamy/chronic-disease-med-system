package com.med.auth.controller;

import com.med.auth.entity.User;
import com.med.auth.exception.BusinessException;
import com.med.auth.service.UserService;
import com.med.auth.util.JwtUtil;
import com.med.common.result.Result;
import lombok.Data;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private UserService userService;

    @Resource
    private JwtUtil jwtUtil;

    @Data
    public static class LoginParam {
        private String username;
        private String password;
    }

    // 原有登录接口
    @PostMapping("/login")
    public Result<String> login(LoginParam param) {
        User user = userService.login(param.getUsername(), param.getPassword());
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        return Result.ok(token);
    }

    // 测试拦截接口
    @GetMapping("/test")
    public Result<String> test() {
        return Result.ok("需要登录才能访问的接口，访问成功");
    }

    // ============ 注册接口 ============
    @Data
    public static class RegisterParam {
        private String username;
        private String password;
        private String phone;
    }

    @PostMapping("/register")
    public Result<String> register(RegisterParam param) {
        if(param.getUsername() == null || param.getUsername().isBlank()
                || param.getPassword() == null || param.getPassword().isBlank()){
            throw new BusinessException("用户名和密码不能为空");
        }
        User user = new User();
        user.setUsername(param.getUsername());
        user.setPassword(param.getPassword());
        user.setPhone(param.getPhone());
        // 已删除手动设置时间代码
        userService.register(user);
        return Result.ok("注册成功");
    }

    // ============ 退出登录接口 ============
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.ok("退出登录成功，请清除本地Token");
    }

    // ====================== 个人中心新增接口 ======================
    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/info")
    public Result<User> getUserInfo(HttpServletRequest request) {
        System.out.println("【1】进入 /auth/info 接口");
        String token = jwtUtil.getTokenFromRequest(request);
        System.out.println("【2】提取到token = " + token);
        String username = jwtUtil.getUsernameByToken(token);
        System.out.println("【3】解析出用户名 = " + username);
        User user = userService.getByUsername(username);
        System.out.println("【4】查询到用户对象 = " + user);
        // 密码脱敏
        user.setPassword(null);
        return Result.ok(user);
    }
    /**
     * 修改个人基础信息（手机号、昵称等）
     */
    @Data
    public static class UpdateInfoParam {
        private String phone;
        // 如果有nickname字段自行增加
        // private String nickname;
    }
    @PostMapping("/updateInfo")
    public Result<String> updateInfo(UpdateInfoParam param, HttpServletRequest request) {
        String token = jwtUtil.getTokenFromRequest(request);
        Long userId = jwtUtil.getUserIdByToken(token);
        User user = new User();
        user.setId(userId);
        user.setPhone(param.getPhone());
        userService.updateUserInfo(user);
        return Result.ok("个人信息修改成功");
    }

    /**
     * 修改密码
     */
    @Data
    public static class UpdatePwdParam {
        private String oldPassword;
        private String newPassword;
    }
    @PostMapping("/updatePwd")
    public Result<String> updatePwd(UpdatePwdParam param, HttpServletRequest request) {
        if(param.getOldPassword() == null || param.getOldPassword().isBlank()
                || param.getNewPassword() == null || param.getNewPassword().isBlank()){
            throw new BusinessException("原密码和新密码不能为空");
        }
        String token = jwtUtil.getTokenFromRequest(request);
        Long userId = jwtUtil.getUserIdByToken(token);
        userService.updatePassword(userId, param.getOldPassword(), param.getNewPassword());
        return Result.ok("密码修改成功，请重新登录");
    }
}