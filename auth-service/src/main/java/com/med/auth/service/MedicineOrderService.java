package com.med.auth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.med.auth.entity.MedicineOrder;
import com.med.common.result.Result;
import java.util.List;

public interface MedicineOrderService extends IService<MedicineOrder> {
    //1 分页查询医嘱
    Result<IPage<MedicineOrder>> pageOrder(Long loginUserId, Long pageNum, Long pageSize, Long patientId);
    //2 新增医嘱
    Result<Void> addOrder(MedicineOrder order, Long loginUserId);
    //3 修改医嘱
    Result<Void> updateOrder(MedicineOrder order, Long loginUserId);
    //4 删除单条医嘱
    Result<Void> deleteOrder(Long id, Long loginUserId);
    //5 批量删除医嘱
    Result<Void> batchDelete(List<Long> ids, Long loginUserId);
    //6 根据ID查询详情
    Result<MedicineOrder> getOrderById(Long id, Long loginUserId);
    //7 指定患者全部医嘱列表
    Result<List<MedicineOrder>> listByPatient(Long patientId, Long loginUserId);
    //8 去重统计所有药品名称
    Result<List<String>> distinctMedicineName(Long loginUserId);
    //9 按月统计医嘱数量
    Result<Long> countByMonth(Long loginUserId, String yearMonth);
    //10 本人医嘱总数统计
    Result<Long> totalCount(Long loginUserId);
    //11 用药提醒简易筛选
    Result<List<MedicineOrder>> remindList(Long loginUserId);
}