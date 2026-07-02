package com.med.auth.util;

import com.med.auth.entity.SysUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET = "ChronicMedSecretKey1234567890000000";
    private static final long EXPIRE = 1000 * 60 * 60 * 24;
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    private static final ThreadLocal<SysUser> USER_LOCAL = new ThreadLocal<>();

    // 设置登录用户
    public static void setLoginUser(SysUser user) {
        USER_LOCAL.set(user);
    }

    // 获取登录用户
    public static SysUser getLoginUser() {
        return USER_LOCAL.get();
    }

    // 清除
    public static void clearLoginUser() {
        USER_LOCAL.remove();
    }

    // 生成Token
    public static String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .signWith(KEY)
                .compact();
    }

    // 解析Token获取UserId
    public static Long getUserIdByToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    // 校验Token
    public static boolean verifyToken(String token) {
        try {
            getUserIdByToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}