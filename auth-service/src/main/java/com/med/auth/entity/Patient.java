package com.med.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("patient")
public class Patient {
    @TableId(type = IdType.AUTO)
    private Long id;
    // 创建医护ID（数据归属，越权校验核心）
    private Long userId;
    private String realName;
    private Integer age;
    private String gender;
    private String phone;
    private String chronicType;
    private String address;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}