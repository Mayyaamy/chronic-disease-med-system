package com.med.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.med.auth.entity.MedicineOrder;
import com.med.auth.entity.SysUser;
import com.med.auth.mapper.MedicineOrderMapper;
import com.med.auth.service.MedicineOrderService;
import com.med.auth.service.PatientService;
import com.med.auth.service.SysUserService;
import com.med.common.result.Result;
import org.springframework.stereotype.Service;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicineOrderServiceImpl extends ServiceImpl<MedicineOrderMapper, MedicineOrder> implements MedicineOrderService {

    private final SysUserService sysUserService;
    private final PatientService patientService;

    public MedicineOrderServiceImpl(SysUserService sysUserService, PatientService patientService) {
        this.sysUserService = sysUserService;
        this.patientService = patientService;
    }

    @Override
    public void addOrder(MedicineOrder order) {
        SysUser login = sysUserService.getLoginUser();
        patientService.checkPatientOwner(order.getPatientId());
        order.setUserId(login.getId());
        save(order);
    }

    @Override
    public void updateOrder(MedicineOrder order) {
        checkOrderOwner(order.getId());
        patientService.checkPatientOwner(order.getPatientId());
        updateById(order);
    }

    @Override
    public void deleteOrder(Long id) {
        checkOrderOwner(id);
        removeById(id);
    }

    @Override
    public MedicineOrder getOrderById(Long id) {
        checkOrderOwner(id);
        return getById(id);
    }

    @Override
    public Result<?> orderPage(Long patientId, Long pageNum, Long pageSize) {
        patientService.checkPatientOwner(patientId);
        IPage<MedicineOrder> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<MedicineOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MedicineOrder::getPatientId, patientId);
        page(page, wrapper);
        return Result.success(page);
    }

    @Override
    public void checkOrderOwner(Long orderId) {
        if (orderId == null) throw new RuntimeException("医嘱ID不能为空");
        MedicineOrder order = getById(orderId);
        if (order == null) throw new RuntimeException("用药医嘱不存在");
        SysUser login = sysUserService.getLoginUser();
        if (!order.getUserId().equals(login.getId())) {
            throw new RuntimeException("无权限操作该用药医嘱");
        }
    }

    // ========== 新增6实现 ==========
    @Override
    public Long countTotalOrder() {
        SysUser login = sysUserService.getLoginUser();
        LambdaQueryWrapper<MedicineOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MedicineOrder::getUserId, login.getId());
        return count(wrapper);
    }

    @Override
    public List<String> getDistinctMedicineName() {
        SysUser login = sysUserService.getLoginUser();
        LambdaQueryWrapper<MedicineOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MedicineOrder::getUserId, login.getId()).select(MedicineOrder::getMedicineName);
        return list(wrapper).stream().map(MedicineOrder::getMedicineName).distinct().collect(Collectors.toList());
    }

    @Override
    public MedicineOrder getLatestOrderByPatient(Long patientId) {
        patientService.checkPatientOwner(patientId);
        LambdaQueryWrapper<MedicineOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MedicineOrder::getPatientId, patientId).orderByDesc(MedicineOrder::getCreateTime);
        return getOne(wrapper);
    }

    @Override
    public Result<?> getExpireSoonOrder() {
        SysUser login = sysUserService.getLoginUser();
        LambdaQueryWrapper<MedicineOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MedicineOrder::getUserId, login.getId());
        return Result.success(list(wrapper));
    }

    @Override
    public void batchDeleteOrder(List<Long> ids) {
        for(Long id : ids) checkOrderOwner(id);
        removeByIds(ids);
    }

    @Override
    public Result<?> monthOrderStat(String yearMonth) {
        SysUser login = sysUserService.getLoginUser();
        YearMonth ym = YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyy-MM"));
        LambdaQueryWrapper<MedicineOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MedicineOrder::getUserId, login.getId())
                .ge(MedicineOrder::getCreateTime, ym.atDay(1))
                .le(MedicineOrder::getCreateTime, ym.atEndOfMonth());
        return Result.success(list(wrapper));
    }
}