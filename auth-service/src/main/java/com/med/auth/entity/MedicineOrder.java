package com.med.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("medicine_order")
public class MedicineOrder {
    @TableId(type = IdType.AUTO)
    private Long id;
    // 创建医护ID（数据归属、越权校验）
    private Long userId;
    // 所属患者ID
    private Long patientId;
    private String medicineName;
    private String dosage;
    private String frequency;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}