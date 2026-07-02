package com.med.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.med.auth.entity.PatientFollow;
import com.med.common.result.Result;
import java.util.List;

public interface PatientFollowService extends IService<PatientFollow> {
    void addFollow(PatientFollow follow);
    void updateFollow(PatientFollow follow);
    void deleteFollow(Long id);
    PatientFollow getFollowById(Long id);
    Result<?> followPage(Long patientId, Long pageNum, Long pageSize);
    Result<?> todayPendingList();
    Result<?> overdueList();
    void finishFollow(Long followId);
    void checkFollowOwner(Long followId);

    // 新增6方法
    Long countTotalFollow();
    Long countFinishFollow();
    Long countUnfinishFollow();
    Result<?> monthFollowList(String yearMonth);
    void batchDeleteFollow(List<Long> ids);
    PatientFollow getLatestFollowByPatient(Long patientId);
}