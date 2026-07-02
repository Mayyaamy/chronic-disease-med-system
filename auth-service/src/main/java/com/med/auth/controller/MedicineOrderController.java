package com.med.auth.controller;

import com.med.auth.entity.MedicineOrder;
import com.med.auth.service.MedicineOrderService;
import com.med.common.result.Result;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/medicineOrder")
public class MedicineOrderController {

    private final MedicineOrderService orderService;

    public MedicineOrderController(MedicineOrderService orderService) {
        this.orderService = orderService;
    }

    // ========== 原有5个接口 ==========
    @PostMapping("/add")
    public Result<String> addOrder(@RequestBody MedicineOrder order) {
        orderService.addOrder(order);
        return Result.success("用药医嘱新增成功");
    }

    @PostMapping("/update")
    public Result<String> updateOrder(@RequestBody MedicineOrder order) {
        orderService.updateOrder(order);
        return Result.success("用药医嘱修改成功");
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteOrder(@PathVariable(value = "id") Long id) {
        orderService.deleteOrder(id);
        return Result.success("用药医嘱删除成功");
    }

    @GetMapping("/{id}")
    public Result<MedicineOrder> getOrder(@PathVariable(value = "id") Long id) {
        return Result.success(orderService.getOrderById(id));
    }

    @GetMapping("/page")
    public Result<?> pageOrder(
            @RequestParam(value = "patientId") Long patientId,
            @RequestParam(value = "pageNum") Long pageNum,
            @RequestParam(value = "pageSize") Long pageSize
    ) {
        return orderService.orderPage(patientId, pageNum, pageSize);
    }

    // ========== 新增6个接口（凑齐11个） ==========
    @GetMapping("/count/total")
    public Result<Long> countTotal() {
        return Result.success(orderService.countTotalOrder());
    }

    @GetMapping("/list/medicine-name")
    public Result<List<String>> distinctMedicine() {
        return Result.success(orderService.getDistinctMedicineName());
    }

    @GetMapping("/latest/{patientId}")
    public Result<MedicineOrder> latestOrder(@PathVariable(value = "patientId") Long patientId) {
        return Result.success(orderService.getLatestOrderByPatient(patientId));
    }

    @GetMapping("/expire-soon")
    public Result<?> expireSoonList() {
        return Result.success(orderService.getExpireSoonOrder());
    }

    @DeleteMapping("/batch")
    public Result<String> batchDelete(@RequestBody List<Long> ids) {
        orderService.batchDeleteOrder(ids);
        return Result.success("批量删除医嘱成功");
    }

    @GetMapping("/month-stat")
    public Result<?> monthStat(@RequestParam(value = "yearMonth") String yearMonth) {
        return orderService.monthOrderStat(yearMonth);
    }
}