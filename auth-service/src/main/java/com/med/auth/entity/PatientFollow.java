package com.med.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("patient_follow")
public class PatientFollow {
    @TableId(type = IdType.AUTO)
    private Long id;
    // 创建医护ID（越权校验）
    private Long userId;
    // 所属患者ID
    private Long patientId;
    private LocalDate followDate;
    private String bloodPressure;
    private String bloodSugar;
    private String medicineRecord;
    private String doctorSuggest;
    // 0待完成 1已完成
    private Integer finish;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}