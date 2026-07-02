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
    private Long userId;
    private Long patientId;
    private LocalDate followDate;
    private String bloodPressure;
    private String bloodSugar;
    private String medicineRecord;
    private String doctorSuggest;
    private Integer finish;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}