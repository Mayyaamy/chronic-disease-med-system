package com.med.auth.interceptor;

import com.med.auth.util.JwtUtil;
import com.med.common.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class TokenInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    // 构造器注入
    public TokenInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = jwtUtil.getTokenFromRequest(request);
        if (token == null) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(200);
            response.getWriter().write(OBJECT_MAPPER.writeValueAsString(Result.fail("未登录，请先登录")));
            return false;
        }
        if (!jwtUtil.validateToken(token)) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(200);
            response.getWriter().write(OBJECT_MAPPER.writeValueAsString(Result.fail("登录已失效，请重新登录")));
            return false;
        }
        return true;
    }
}