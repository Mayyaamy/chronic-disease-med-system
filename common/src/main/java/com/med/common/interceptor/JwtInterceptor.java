package com.med.common.interceptor;

import com.med.common.exception.BusinessException;
import com.med.common.result.ResultCodeEnum;
import com.med.common.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取请求头token
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            throw new BusinessException(ResultCodeEnum.UNAUTHORIZED);
        }
        if (!JwtUtil.verifyToken(token)) {
            throw new BusinessException(ResultCodeEnum.UNAUTHORIZED);
        }
        // 解析用户ID存入请求域，后续接口可直接获取当前登录人
        Long userId = JwtUtil.getUserIdByToken(token);
        request.setAttribute("loginUserId", userId);
        return true;
    }
}