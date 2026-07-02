package com.med.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.med.auth.entity.Patient;
import com.med.auth.mapper.PatientMapper;
import com.med.auth.service.PatientService;
import com.med.common.exception.BusinessException;
import com.med.common.result.Result;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    @Override
    public Result<IPage<Patient>> pagePatient(Long userId, Long pageNum, Long pageSize, String keyword) {
        Page<Patient> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Patient::getUserId, userId);
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Patient::getRealName, keyword);
        }
        IPage<Patient> res = page(page, wrapper);
        return Result.success(res);
    }

    @Override
    public Result<Void> addPatient(Patient patient, Long loginUserId) {
        patient.setUserId(loginUserId);
        save(patient);
        return Result.success();
    }

    @Override
    public Result<Void> updatePatient(Patient patient, Long loginUserId) {
        Patient db = getById(patient.getId());
        if (db == null) throw new BusinessException("数据不存在");
        if (!db.getUserId().equals(loginUserId)) throw new BusinessException(403, "无权修改他人数据");
        patient.setUserId(null);
        updateById(patient);
        return Result.success();
    }

    @Override
    public Result<Void> deletePatient(Long id, Long loginUserId) {
        Patient db = getById(id);
        if (db == null) throw new BusinessException("数据不存在");
        if (!db.getUserId().equals(loginUserId)) throw new BusinessException(403, "无权删除他人数据");
        removeById(id);
        return Result.success();
    }

    @Override
    public Result<Void> batchDelete(List<Long> ids, Long loginUserId) {
        for (Long id : ids) {
            deletePatient(id, loginUserId);
        }
        return Result.success();
    }

    @Override
    public Result<Patient> getPatientById(Long id, Long loginUserId) {
        Patient db = getById(id);
        if (db == null) throw new BusinessException("数据不存在");
        if (!db.getUserId().equals(loginUserId)) throw new BusinessException(403, "无权查看他人数据");
        return Result.success(db);
    }

    @Override
    public Result<Long> countByChronicType(Long userId, String type) {
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Patient::getUserId, userId).like(Patient::getChronicType, type);
        return Result.success(count(wrapper));
    }

    @Override
    public Result<List<Patient>> getNameList(Long userId) {
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Patient::getUserId, userId);
        wrapper.select(Patient::getId, Patient::getRealName);
        return Result.success(list(wrapper));
    }

    @Override
    public Result<Double> getAvgAge(Long userId) {
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Patient::getUserId, userId);
        List<Patient> list = list(wrapper);
        if (list.isEmpty()) return Result.success(0.0);
        double sum = list.stream().mapToInt(p -> Optional.ofNullable(p.getAge()).orElse(0)).sum();
        return Result.success(sum / list.size());
    }

    @Override
    public Result<Patient> getMaxAgePatient(Long userId) {
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Patient::getUserId, userId).orderByDesc(Patient::getAge).last("limit 1");
        return Result.success(getOne(wrapper));
    }

    @Override
    public Result<Patient> getLatestPatient(Long userId) {
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Patient::getUserId, userId).orderByDesc(Patient::getCreateTime).last("limit 1");
        return Result.success(getOne(wrapper));
    }

    @Override
    public Result<List<Patient>> searchByPhone(Long userId, String phone) {
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Patient::getUserId, userId).like(Patient::getPhone, phone);
        return Result.success(list(wrapper));
    }

    @Override
    public Result<Long> totalCount(Long userId) {
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Patient::getUserId, userId);
        return Result.success(count(wrapper));
    }
}