package com.med.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.med.auth.entity.Patient;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.med.common.result.Result;
import java.util.List;

public interface PatientService extends IService<Patient> {
    // 分页查询当前医护自己的患者
    Result<IPage<Patient>> pagePatient(Long userId, Long pageNum, Long pageSize, String keyword);
    // 新增患者
    Result<Void> addPatient(Patient patient, Long loginUserId);
    // 修改患者（校验归属）
    Result<Void> updatePatient(Patient patient, Long loginUserId);
    // 删除单个
    Result<Void> deletePatient(Long id, Long loginUserId);
    // 批量删除
    Result<Void> batchDelete(List<Long> ids, Long loginUserId);
    // 根据ID查询详情（归属校验）
    Result<Patient> getPatientById(Long id, Long loginUserId);
    // 按慢病类型统计数量
    Result<Long> countByChronicType(Long userId, String type);
    // 获取当前医护所有患者姓名下拉列表
    Result<List<Patient>> getNameList(Long userId);
    // 查询患者平均年龄
    Result<Double> getAvgAge(Long userId);
    // 查询年龄最大患者
    Result<Patient> getMaxAgePatient(Long userId);
    // 查询最新创建患者
    Result<Patient> getLatestPatient(Long userId);
    // 手机号模糊检索患者
    Result<List<Patient>> searchByPhone(Long userId, String phone);
    // 统计当前医护总患者数量
    Result<Long> totalCount(Long userId);
}