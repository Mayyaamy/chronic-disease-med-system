package com.med.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.med.auth.entity.Patient;
import com.med.auth.entity.PatientFollow;
import com.med.auth.exception.BusinessException;
import com.med.auth.mapper.PatientFollowMapper;
import com.med.auth.mapper.PatientMapper;
import com.med.auth.service.PatientFollowService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class PatientFollowServiceImpl extends ServiceImpl<PatientFollowMapper, PatientFollow>
        implements PatientFollowService {

    @Resource
    private PatientMapper patientMapper;

    /**
     * 校验当前登录人是否拥有该患者操作权限
     */
    private void checkPatientOwner(Long patientId, Long loginUserId) {
        Patient patient = patientMapper.selectById(patientId);
        if (patient == null) {
            throw new BusinessException("对应患者档案不存在");
        }
        if (!patient.getUserId().equals(loginUserId)) {
            throw new BusinessException("无权操作他人患者的随访记录");
        }
    }

    @Override
    public void addFollow(PatientFollow follow, Long loginUserId) {
        // 校验患者归属
        checkPatientOwner(follow.getPatientId(), loginUserId);
        follow.setUserId(loginUserId);
        save(follow);
    }

    @Override
    public void updateFollow(PatientFollow follow, Long loginUserId) {
        PatientFollow dbFollow = getById(follow.getId());
        if (dbFollow == null) {
            throw new BusinessException("随访记录不存在");
        }
        // 校验随访归属本人
        if (!dbFollow.getUserId().equals(loginUserId)) {
            throw new BusinessException("无权修改他人随访记录");
        }
        // 校验患者归属
        checkPatientOwner(dbFollow.getPatientId(), loginUserId);
        follow.setUserId(null);
        follow.setPatientId(null);
        updateById(follow);
    }

    @Override
    public void deleteFollow(Long followId, Long loginUserId) {
        PatientFollow dbFollow = getById(followId);
        if (dbFollow == null) {
            throw new BusinessException("随访记录不存在");
        }
        if (!dbFollow.getUserId().equals(loginUserId)) {
            throw new BusinessException("无权删除他人随访记录");
        }
        checkPatientOwner(dbFollow.getPatientId(), loginUserId);
        removeById(followId);
    }

    @Override
    public PatientFollow getFollowById(Long followId, Long loginUserId) {
        PatientFollow dbFollow = getById(followId);
        if (dbFollow == null) {
            throw new BusinessException("随访记录不存在");
        }
        if (!dbFollow.getUserId().equals(loginUserId)) {
            throw new BusinessException("无权查看他人随访记录");
        }
        checkPatientOwner(dbFollow.getPatientId(), loginUserId);
        return dbFollow;
    }

    @Override
    public Page<PatientFollow> pageFollowByPatient(Long patientId, Long loginUserId, Long pageNum, Long pageSize) {
        checkPatientOwner(patientId, loginUserId);
        Page<PatientFollow> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PatientFollow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PatientFollow::getPatientId, patientId);
        wrapper.eq(PatientFollow::getUserId, loginUserId);
        return page(page, wrapper);
    }
}