package com.med.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.med.auth.entity.Patient;
import com.med.auth.entity.PatientFollow;
import com.med.auth.entity.SysUser;
import com.med.auth.mapper.PatientFollowMapper;
import com.med.auth.service.PatientFollowService;
import com.med.auth.service.PatientService;
import com.med.auth.service.SysUserService;
import com.med.common.result.Result;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PatientFollowServiceImpl extends ServiceImpl<PatientFollowMapper, PatientFollow> implements PatientFollowService {

    private final SysUserService sysUserService;
    private final PatientService patientService;

    public PatientFollowServiceImpl(SysUserService sysUserService, PatientService patientService) {
        this.sysUserService = sysUserService;
        this.patientService = patientService;
    }

    @Override
    public void addFollow(PatientFollow follow) {
        SysUser login = sysUserService.getLoginUser();
        // 校验患者归属
        patientService.checkPatientOwner(follow.getPatientId());
        follow.setUserId(login.getId());
        follow.setFinish(0);
        save(follow);
    }

    @Override
    public void updateFollow(PatientFollow follow) {
        checkFollowOwner(follow.getId());
        // 不能把随访修改为别人的患者
        patientService.checkPatientOwner(follow.getPatientId());
        updateById(follow);
    }

    @Override
    public void deleteFollow(Long id) {
        checkFollowOwner(id);
        removeById(id);
    }

    @Override
    public PatientFollow getFollowById(Long id) {
        checkFollowOwner(id);
        return getById(id);
    }

    @Override
    public Result<?> followPage(Long patientId, Long pageNum, Long pageSize) {
        patientService.checkPatientOwner(patientId);
        IPage<PatientFollow> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PatientFollow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PatientFollow::getPatientId, patientId);
        page(page, wrapper);
        return Result.success(page);
    }

    @Override
    public Result<?> todayPendingList() {
        SysUser login = sysUserService.getLoginUser();
        LocalDate now = LocalDate.now();
        LambdaQueryWrapper<PatientFollow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PatientFollow::getUserId, login.getId())
                .eq(PatientFollow::getFinish, 0)
                .eq(PatientFollow::getFollowDate, now);
        return Result.success(list(wrapper));
    }

    @Override
    public Result<?> overdueList() {
        SysUser login = sysUserService.getLoginUser();
        LocalDate now = LocalDate.now();
        LambdaQueryWrapper<PatientFollow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PatientFollow::getUserId, login.getId())
                .eq(PatientFollow::getFinish, 0)
                .lt(PatientFollow::getFollowDate, now);
        return Result.success(list(wrapper));
    }

    @Override
    public void finishFollow(Long followId) {
        checkFollowOwner(followId);
        PatientFollow follow = getById(followId);
        follow.setFinish(1);
        updateById(follow);
    }

    @Override
    public void checkFollowOwner(Long followId) {
        if (followId == null) throw new RuntimeException("随访ID不能为空");
        PatientFollow follow = getById(followId);
        if (follow == null) throw new RuntimeException("随访记录不存在");
        SysUser login = sysUserService.getLoginUser();
        if (!follow.getUserId().equals(login.getId())) {
            throw new RuntimeException("无权限操作该随访记录");
        }
    }

    // ===================== 新增接口实现（修复抽象方法未实现报错） =====================
    @Override
    public Long countTotalFollow() {
        SysUser login = sysUserService.getLoginUser();
        LambdaQueryWrapper<PatientFollow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PatientFollow::getUserId, login.getId());
        return count(wrapper);
    }

    @Override
    public Long countFinishFollow() {
        SysUser login = sysUserService.getLoginUser();
        LambdaQueryWrapper<PatientFollow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PatientFollow::getUserId, login.getId()).eq(PatientFollow::getFinish,1);
        return count(wrapper);
    }

    @Override
    public Long countUnfinishFollow() {
        SysUser login = sysUserService.getLoginUser();
        LambdaQueryWrapper<PatientFollow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PatientFollow::getUserId, login.getId()).eq(PatientFollow::getFinish,0);
        return count(wrapper);
    }

    @Override
    public Result<?> monthFollowList(String yearMonth) {
        SysUser login = sysUserService.getLoginUser();
        YearMonth ym = YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyy-MM"));
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();
        LambdaQueryWrapper<PatientFollow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PatientFollow::getUserId, login.getId())
                .ge(PatientFollow::getFollowDate, start)
                .le(PatientFollow::getFollowDate, end);
        return Result.success(list(wrapper));
    }

    @Override
    public void batchDeleteFollow(List<Long> ids) {
        // 逐条校验每条随访归属，防止越权删除
        for(Long id : ids) {
            checkFollowOwner(id);
        }
        // List属于Collection子类，参数完全兼容，消除类型提示警告
        removeByIds(ids);
    }

    @Override
    public PatientFollow getLatestFollowByPatient(Long patientId) {
        patientService.checkPatientOwner(patientId);
        LambdaQueryWrapper<PatientFollow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PatientFollow::getPatientId, patientId);
        wrapper.orderByDesc(PatientFollow::getFollowDate);
        return getOne(wrapper);
    }
}