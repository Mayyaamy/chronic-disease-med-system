package com.med.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.med.auth.entity.Patient;
import com.med.auth.exception.BusinessException;
import com.med.auth.mapper.PatientMapper;
import com.med.auth.service.PatientService;
import org.springframework.stereotype.Service;

@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    @Override
    public void addPatient(Patient patient, Long loginUserId) {
        patient.setUserId(loginUserId);
        save(patient);
    }

    @Override
    public void updatePatient(Patient patient, Long loginUserId) {
        Patient dbPatient = getById(patient.getId());
        if (dbPatient == null) {
            throw new BusinessException("该患者档案不存在");
        }
        if (!dbPatient.getUserId().equals(loginUserId)) {
            throw new BusinessException("无权修改他人档案");
        }
        patient.setUserId(null);
        updateById(patient);
    }

    @Override
    public void deletePatient(Long patientId, Long loginUserId) {
        Patient dbPatient = getById(patientId);
        if (dbPatient == null) {
            throw new BusinessException("该患者档案不存在");
        }
        if (!dbPatient.getUserId().equals(loginUserId)) {
            throw new BusinessException("无权删除他人档案");
        }
        removeById(patientId);
    }

    @Override
    public Patient getPatientById(Long patientId, Long loginUserId) {
        Patient dbPatient = getById(patientId);
        if (dbPatient == null) {
            throw new BusinessException("该患者档案不存在");
        }
        if (!dbPatient.getUserId().equals(loginUserId)) {
            throw new BusinessException("无权查看他人档案");
        }
        return dbPatient;
    }

    @Override
    public Page<Patient> pagePatient(Long loginUserId, Long pageNum, Long pageSize) {
        Page<Patient> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Patient::getUserId, loginUserId);
        return page(page, wrapper);
    }
}