package com.med.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.med.auth.entity.MedicineOrder;
import com.med.common.result.Result;
import java.util.List;

public interface MedicineOrderService extends IService<MedicineOrder> {
    void addOrder(MedicineOrder order);
    void updateOrder(MedicineOrder order);
    void deleteOrder(Long id);
    MedicineOrder getOrderById(Long id);
    Result<?> orderPage(Long patientId, Long pageNum, Long pageSize);
    void checkOrderOwner(Long orderId);

    // 新增6方法
    Long countTotalOrder();
    List<String> getDistinctMedicineName();
    MedicineOrder getLatestOrderByPatient(Long patientId);
    Result<?> getExpireSoonOrder();
    void batchDeleteOrder(List<Long> ids);
    Result<?> monthOrderStat(String yearMonth);
}