package com.med.auth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.med.auth.entity.PatientFollow;
import com.med.common.result.Result;
import java.util.List;

public interface PatientFollowService extends IService<PatientFollow> {
    // 1 分页查询随访
    Result<IPage<PatientFollow>> pageFollow(Long loginUserId, Long pageNum, Long pageSize, Long patientId);
    // 2 新增随访
    Result<Void> addFollow(PatientFollow follow, Long loginUserId);
    // 3 修改随访
    Result<Void> updateFollow(PatientFollow follow, Long loginUserId);
    // 4 删除单条随访
    Result<Void> deleteFollow(Long id, Long loginUserId);
    // 5 批量删除随访
    Result<Void> batchDelete(List<Long> ids, Long loginUserId);
    // 6 根据ID查询详情
    Result<PatientFollow> getFollowById(Long id, Long loginUserId);
    // 7 查询待完成随访列表
    Result<List<PatientFollow>> getPendingList(Long loginUserId);
    // 8 查询已完成随访列表
    Result<List<PatientFollow>> getFinishedList(Long loginUserId);
    // 9 按月统计随访数据
    Result<Long> countByMonth(Long loginUserId, String yearMonth);
    // 10 标记随访已完成
    Result<Void> finishFollow(Long id, Long loginUserId);
    // 11 查询指定患者全部随访
    Result<List<PatientFollow>> listByPatient(Long patientId, Long loginUserId);
    // 12 获取该患者最近一条随访记录
    Result<PatientFollow> latestFollow(Long patientId, Long loginUserId);
    // 13 统计本人总随访条数
    Result<Long> totalFollowCount(Long loginUserId);
    // 14 统计未完成随访数量
    Result<Long> pendingFollowCount(Long loginUserId);
}