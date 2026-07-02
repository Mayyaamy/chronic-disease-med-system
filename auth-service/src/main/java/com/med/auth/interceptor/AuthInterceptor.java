package com.med.auth.interceptor;

import com.med.common.exception.BusinessException;
import com.med.common.result.ResultCodeEnum;
import com.med.common.util.JwtUtil;
import com.med.common.util.RedisUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new BusinessException(ResultCodeEnum.TOKEN_NULL);
        }
        String realToken = token.substring(7);
        // 校验Token有效性
        if (!jwtUtil.verifyToken(realToken)) {
            throw new BusinessException(ResultCodeEnum.TOKEN_INVALID);
        }
        Long userId = jwtUtil.getUserIdByToken(realToken);
        // Redis校验登录状态，实现登录挤下线
        if (!redisUtil.hasKey("login:user:" + userId)) {
            throw new BusinessException(ResultCodeEnum.TOKEN_INVALID);
        }
        // 存入当前登录用户ID，Controller可获取
        request.setAttribute("currentUserId", userId);
        return true;
    }
}