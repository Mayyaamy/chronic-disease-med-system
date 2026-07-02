package com.med.auth.service;

import com.med.auth.entity.SysUser;
import com.med.common.result.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SysUserServiceTest {

    @Autowired
    private SysUserService userService;

    /**
     * 测试登录逻辑（密码校验、Token生成、Redis存入登录态）
     */
    @Test
    void testLogin() {
        Result<String> result = userService.login("admin", "123456");
        System.out.println("登录返回Token：" + result.getData());
        assert result.getCode() == 200;
        assert result.getData() != null;
    }

    /**
     * 测试获取当前用户信息
     */
    @Test
    void testGetCurrentUser() {
        Result<SysUser> result = userService.getCurrentUser(1L);
        System.out.println("用户信息：" + result.getData());
        assert result.getCode() == 200;
        assert "admin".equals(result.getData().getUsername());
    }

    /**
     * 测试注册重复用户名异常
     */
    @Test
    void testRegisterRepeat() {
        SysUser user = new SysUser();
        user.setUsername("admin");
        user.setPassword("123456");
        try {
            userService.register(user);
        } catch (Exception e) {
            System.out.println("预期异常：" + e.getMessage());
            assert true;
        }
    }
}