package com.med.auth.interceptor;

import com.med.auth.entity.SysUser;
import com.med.auth.service.SysUserService;
import com.med.auth.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final SysUserService sysUserService;

    public JwtInterceptor(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(401);
            return false;
        }
        token = token.substring(7);
        if (!JwtUtil.verifyToken(token)) {
            response.setStatus(401);
            return false;
        }
        Long userId = JwtUtil.getUserIdByToken(token);
        SysUser user = sysUserService.getById(userId);
        if (user == null || user.getStatus() == 0) {
            response.setStatus(401);
            return false;
        }
        JwtUtil.setLoginUser(user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        JwtUtil.clearLoginUser();
    }
}