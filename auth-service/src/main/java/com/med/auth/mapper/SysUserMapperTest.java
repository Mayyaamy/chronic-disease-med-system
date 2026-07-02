package com.med.auth.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.med.auth.entity.SysUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SysUserMapperTest {

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 测试根据用户名查询用户（登录查询核心SQL）
     */
    @Test
    void testSelectByUsername() {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, "admin");
        SysUser user = sysUserMapper.selectOne(wrapper);
        System.out.println("查询结果：" + user);
        assert user != null;
        assert "admin".equals(user.getUsername());
    }

    /**
     * 测试查询全部用户
     */
    @Test
    void testSelectList() {
        List<SysUser> list = sysUserMapper.selectList(null);
        System.out.println("用户总数：" + list.size());
        assert list.size() > 0;
    }
}