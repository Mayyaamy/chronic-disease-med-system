package com.med.auth.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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

    // 创建人ID
    private Long userId;
    // 所属患者ID
    private Long patientId;
    // 随访日期
    private LocalDate followDate;
    // 血压
    private String bloodPressure;
    // 血糖
    private String bloodSugar;
    // 用药记录
    private String medicineRecord;
    // 医生建议
    private String doctorSuggest;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}