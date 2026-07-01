package com.med.auth.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.med.auth.entity.Patient;

public interface PatientService {

    // 新增患者档案
    void addPatient(Patient patient, Long loginUserId);

    // 修改患者档案（校验归属本人）
    void updatePatient(Patient patient, Long loginUserId);

    // 删除患者档案（校验归属本人）
    void deletePatient(Long patientId, Long loginUserId);

    // 根据ID查询单条详情（校验归属本人）
    Patient getPatientById(Long patientId, Long loginUserId);

    // 分页查询当前登录用户自己的所有患者
    Page<Patient> pagePatient(Long loginUserId, Long pageNum, Long pageSize);
}