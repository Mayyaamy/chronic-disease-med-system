package com.med.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.med.auth.entity.PatientFollow;
import com.med.auth.mapper.PatientFollowMapper;
import com.med.auth.service.PatientFollowService;
import com.med.common.exception.BusinessException;
import com.med.common.result.Result;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PatientFollowServiceImpl extends ServiceImpl<PatientFollowMapper, PatientFollow> implements PatientFollowService {

    @Override
    public Result<IPage<PatientFollow>> pageFollow(Long loginUserId, Long pageNum, Long pageSize, Long patientId) {
        Page<PatientFollow> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PatientFollow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PatientFollow::getUserId, loginUserId);
        if(patientId != null){
            wrapper.eq(PatientFollow::getPatientId, patientId);
        }
        IPage<PatientFollow> pageData = page(page, wrapper);
        return Result.success(pageData);
    }

    @Override
    public Result<Void> addFollow(PatientFollow follow, Long loginUserId) {
        follow.setUserId(loginUserId);
        follow.setFinish(0);
        save(follow);
        return Result.success();
    }

    @Override
    public Result<Void> updateFollow(PatientFollow follow, Long loginUserId) {
        PatientFollow db = getById(follow.getId());
        if(db == null) throw new BusinessException("随访数据不存在");
        if(!db.getUserId().equals(loginUserId)) throw new BusinessException(403, "无权修改他人随访数据");
        follow.setUserId(null);
        updateById(follow);
        return Result.success();
    }

    @Override
    public Result<Void> deleteFollow(Long id, Long loginUserId) {
        PatientFollow db = getById(id);
        if(db == null) throw new BusinessException("随访数据不存在");
        if(!db.getUserId().equals(loginUserId)) throw new BusinessException(403, "无权删除他人随访数据");
        removeById(id);
        return Result.success();
    }

    @Override
    public Result<Void> batchDelete(List<Long> ids, Long loginUserId) {
        for(Long id : ids){
            deleteFollow(id, loginUserId);
        }
        return Result.success();
    }

    @Override
    public Result<PatientFollow> getFollowById(Long id, Long loginUserId) {
        PatientFollow db = getById(id);
        if(db == null) throw new BusinessException("随访数据不存在");
        if(!db.getUserId().equals(loginUserId)) throw new BusinessException(403, "无权查看他人随访数据");
        return Result.success(db);
    }

    @Override
    public Result<List<PatientFollow>> getPendingList(Long loginUserId) {
        LambdaQueryWrapper<PatientFollow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PatientFollow::getUserId, loginUserId)
                .eq(PatientFollow::getFinish, 0);
        return Result.success(list(wrapper));
    }

    @Override
    public Result<List<PatientFollow>> getFinishedList(Long loginUserId) {
        LambdaQueryWrapper<PatientFollow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PatientFollow::getUserId, loginUserId)
                .eq(PatientFollow::getFinish, 1);
        return Result.success(list(wrapper));
    }

    @Override
    public Result<Long> countByMonth(Long loginUserId, String yearMonth) {
        LambdaQueryWrapper<PatientFollow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PatientFollow::getUserId, loginUserId)
                .apply("DATE_FORMAT(follow_date,'%Y-%m') = {0}", yearMonth);
        return Result.success(count(wrapper));
    }

    @Override
    public Result<Void> finishFollow(Long id, Long loginUserId) {
        PatientFollow db = getById(id);
        if(db == null) throw new BusinessException("随访数据不存在");
        if(!db.getUserId().equals(loginUserId)) throw new BusinessException(403, "无权操作他人随访");
        db.setFinish(1);
        updateById(db);
        return Result.success();
    }

    @Override
    public Result<List<PatientFollow>> listByPatient(Long patientId, Long loginUserId) {
        LambdaQueryWrapper<PatientFollow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PatientFollow::getUserId, loginUserId)
                .eq(PatientFollow::getPatientId, patientId)
                .orderByDesc(PatientFollow::getFollowDate);
        return Result.success(list(wrapper));
    }

    @Override
    public Result<PatientFollow> latestFollow(Long patientId, Long loginUserId) {
        LambdaQueryWrapper<PatientFollow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PatientFollow::getUserId, loginUserId)
                .eq(PatientFollow::getPatientId, patientId)
                .orderByDesc(PatientFollow::getFollowDate)
                .last("limit 1");
        return Result.success(getOne(wrapper));
    }

    @Override
    public Result<Long> totalFollowCount(Long loginUserId) {
        LambdaQueryWrapper<PatientFollow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PatientFollow::getUserId, loginUserId);
        return Result.success(count(wrapper));
    }

    @Override
    public Result<Long> pendingFollowCount(Long loginUserId) {
        LambdaQueryWrapper<PatientFollow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PatientFollow::getUserId, loginUserId)
                .eq(PatientFollow::getFinish, 0);
        return Result.success(count(wrapper));
    }
}