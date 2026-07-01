package com.med.auth.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.med.auth.entity.PatientFollow;

public interface PatientFollowService {
    // 新增随访记录
    void addFollow(PatientFollow follow, Long loginUserId);

    // 修改随访记录
    void updateFollow(PatientFollow follow, Long loginUserId);

    // 删除随访记录
    void deleteFollow(Long followId, Long loginUserId);

    // 根据ID查询单条随访详情
    PatientFollow getFollowById(Long followId, Long loginUserId);

    // 分页：查询某个患者的全部随访记录
    Page<PatientFollow> pageFollowByPatient(Long patientId, Long loginUserId, Long pageNum, Long pageSize);
}