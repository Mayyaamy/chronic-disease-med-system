package com.med.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.med.auth.entity.MedicineOrder;
import com.med.auth.mapper.MedicineOrderMapper;
import com.med.auth.service.MedicineOrderService;
import com.med.common.exception.BusinessException;
import com.med.common.result.Result;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicineOrderServiceImpl extends ServiceImpl<MedicineOrderMapper, MedicineOrder> implements MedicineOrderService {

    @Override
    public Result<IPage<MedicineOrder>> pageOrder(Long loginUserId, Long pageNum, Long pageSize, Long patientId) {
        Page<MedicineOrder> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<MedicineOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MedicineOrder::getUserId, loginUserId);
        if (patientId != null) {
            wrapper.eq(MedicineOrder::getPatientId, patientId);
        }
        IPage<MedicineOrder> pageData = page(page, wrapper);
        return Result.success(pageData);
    }

    @Override
    public Result<Void> addOrder(MedicineOrder order, Long loginUserId) {
        order.setUserId(loginUserId);
        save(order);
        return Result.success();
    }

    @Override
    public Result<Void> updateOrder(MedicineOrder order, Long loginUserId) {
        MedicineOrder db = getById(order.getId());
        if (db == null) throw new BusinessException("医嘱数据不存在");
        if (!db.getUserId().equals(loginUserId)) throw new BusinessException(403, "无权修改他人医嘱");
        order.setUserId(null);
        updateById(order);
        return Result.success();
    }

    @Override
    public Result<Void> deleteOrder(Long id, Long loginUserId) {
        MedicineOrder db = getById(id);
        if (db == null) throw new BusinessException("医嘱数据不存在");
        if (!db.getUserId().equals(loginUserId)) throw new BusinessException(403, "无权删除他人医嘱");
        removeById(id);
        return Result.success();
    }

    @Override
    public Result<Void> batchDelete(List<Long> ids, Long loginUserId) {
        for (Long id : ids) {
            deleteOrder(id, loginUserId);
        }
        return Result.success();
    }

    @Override
    public Result<MedicineOrder> getOrderById(Long id, Long loginUserId) {
        MedicineOrder db = getById(id);
        if (db == null) throw new BusinessException("医嘱数据不存在");
        if (!db.getUserId().equals(loginUserId)) throw new BusinessException(403, "无权查看他人医嘱");
        return Result.success(db);
    }

    @Override
    public Result<List<MedicineOrder>> listByPatient(Long patientId, Long loginUserId) {
        LambdaQueryWrapper<MedicineOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MedicineOrder::getUserId, loginUserId)
                .eq(MedicineOrder::getPatientId, patientId)
                .orderByDesc(MedicineOrder::getCreateTime);
        return Result.success(list(wrapper));
    }

    @Override
    public Result<List<String>> distinctMedicineName(Long loginUserId) {
        LambdaQueryWrapper<MedicineOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MedicineOrder::getUserId, loginUserId)
                .select(MedicineOrder::getMedicineName);
        List<MedicineOrder> list = list(wrapper);
        List<String> nameList = list.stream()
                .map(MedicineOrder::getMedicineName)
                .distinct()
                .collect(Collectors.toList());
        return Result.success(nameList);
    }

    @Override
    public Result<Long> countByMonth(Long loginUserId, String yearMonth) {
        LambdaQueryWrapper<MedicineOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MedicineOrder::getUserId, loginUserId)
                .apply("DATE_FORMAT(create_time,'%Y-%m') = {0}", yearMonth);
        return Result.success(count(wrapper));
    }

    @Override
    public Result<Long> totalCount(Long loginUserId) {
        LambdaQueryWrapper<MedicineOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MedicineOrder::getUserId, loginUserId);
        return Result.success(count(wrapper));
    }

    @Override
    public Result<List<MedicineOrder>> remindList(Long loginUserId) {
        LambdaQueryWrapper<MedicineOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MedicineOrder::getUserId, loginUserId)
                .orderByDesc(MedicineOrder::getCreateTime)
                .last("limit 5");
        return Result.success(list(wrapper));
    }
}