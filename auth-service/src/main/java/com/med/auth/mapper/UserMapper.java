package com.med.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.med.auth.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends BaseMapper<User> {

    // 根据用户名查询用户
    User selectByUsername(@Param("username") String username);
}