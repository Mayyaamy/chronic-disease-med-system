package com.med.auth.util;

import org.springframework.util.DigestUtils;

public class PasswordUtil {
    // MD5加密
    public static String encryptPwd(String raw) {
        return DigestUtils.md5DigestAsHex(raw.getBytes());
    }

    // 校验密码
    public static boolean checkPwd(String raw, String encrypted) {
        return encryptPwd(raw).equals(encrypted);
    }
}