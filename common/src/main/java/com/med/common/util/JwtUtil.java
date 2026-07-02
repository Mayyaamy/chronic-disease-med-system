package com.med.common.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    // 密钥，后续配置文件统一修改
    private static final String SECRET_STR = "medSecretKey1234567890MedChronicDiseaseSystem20260627";
    // 过期时间：2小时
    private static final long EXPIRE_TIME = 2 * 60 * 60 * 1000;

    private static SecretKey getSecretKey() {
        byte[] keyBytes = SECRET_STR.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成token，传入用户id
     */
    public static String generateToken(Long userId) {
        long now = System.currentTimeMillis();
        Date expireDate = new Date(now + EXPIRE_TIME);

        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(new Date(now))
                .setExpiration(expireDate)
                .signWith(getSecretKey())
                .compact();
    }

    /**
     * 解析token获取用户ID
     */
    public static Long getUserIdByToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.valueOf(claims.getSubject());
    }

    /**
     * 校验token是否有效
     */
    public static boolean verifyToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
