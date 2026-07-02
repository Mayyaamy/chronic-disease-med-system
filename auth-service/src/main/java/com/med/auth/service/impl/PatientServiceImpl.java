package com.med.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.med.auth.entity.Patient;
import com.med.auth.entity.SysUser;
import com.med.auth.mapper.PatientMapper;
import com.med.auth.service.PatientService;
import com.med.auth.service.SysUserService;
import com.med.common.result.Result;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    private final SysUserService sysUserService;

    public PatientServiceImpl(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Override
    public void addPatient(Patient patient) {
        SysUser login = sysUserService.getLoginUser();
        patient.setUserId(login.getId());
        save(patient);
    }

    @Override
    public void updatePatient(Patient patient) {
        checkPatientOwner(patient.getId());
        updateById(patient);
    }

    @Override
    public void deletePatient(Long id) {
        checkPatientOwner(id);
        removeById(id);
    }

    @Override
    public Patient getPatientById(Long id) {
        checkPatientOwner(id);
        return getById(id);
    }

    @Override
    public Result<?> patientPage(Long pageNum, Long pageSize, String chronicType) {
        SysUser login = sysUserService.getLoginUser();
        IPage<Patient> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Patient::getUserId, login.getId());
        if (chronicType != null && !chronicType.trim().isEmpty()) {
            wrapper.like(Patient::getChronicType, chronicType);
        }
        page(page, wrapper);
        return Result.success(page);
    }

    @Override
    public Long countMyPatient() {
        SysUser login = sysUserService.getLoginUser();
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Patient::getUserId, login.getId());
        return count(wrapper);
    }

    @Override
    public void checkPatientOwner(Long patientId) {
        if (patientId == null) throw new RuntimeException("患者ID不能为空");
        Patient patient = getById(patientId);
        if (patient == null) throw new RuntimeException("患者不存在");
        SysUser login = sysUserService.getLoginUser();
        if (!patient.getUserId().equals(login.getId())) {
            throw new RuntimeException("无权限操作该患者数据");
        }
    }

    // ========== 新增7个实现 ==========
    @Override
    public List<Patient> listPatientNameSimple() {
        SysUser login = sysUserService.getLoginUser();
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Patient::getUserId, login.getId());
        wrapper.select(Patient::getId, Patient::getRealName);
        return list(wrapper);
    }

    @Override
    public Long countPatientByType(String chronicType) {
        SysUser login = sysUserService.getLoginUser();
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Patient::getUserId, login.getId()).like(Patient::getChronicType, chronicType);
        return count(wrapper);
    }

    @Override
    public Double getAvgAge() {
        SysUser login = sysUserService.getLoginUser();
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Patient::getUserId, login.getId()).isNotNull(Patient::getAge);
        List<Patient> list = list(wrapper);
        if(list.isEmpty()) return 0.0;
        double sum = 0;
        for(Patient p : list) sum += p.getAge();
        return sum / list.size();
    }

    @Override
    public Patient getOldestPatient() {
        SysUser login = sysUserService.getLoginUser();
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Patient::getUserId, login.getId()).isNotNull(Patient::getAge);
        wrapper.orderByDesc(Patient::getAge);
        return getOne(wrapper);
    }

    @Override
    public Patient getLatestPatient() {
        SysUser login = sysUserService.getLoginUser();
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Patient::getUserId, login.getId());
        wrapper.orderByDesc(Patient::getCreateTime);
        return getOne(wrapper);
    }

    @Override
    public void batchDeletePatient(List<Long> ids) {
        for(Long id : ids) checkPatientOwner(id);
        removeByIds(ids);
    }

    @Override
    public Patient searchPatientByPhone(String phone) {
        SysUser login = sysUserService.getLoginUser();
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Patient::getUserId, login.getId()).like(Patient::getPhone, phone);
        return getOne(wrapper);
    }
}