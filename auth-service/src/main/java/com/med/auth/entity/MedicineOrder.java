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
    private Long userId;
    private Long patientId;
    private String medicineName;
    private String dosage;
    private String frequency;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}