package com.med.auth.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.med.auth.entity.MedicineOrder;
import com.med.auth.service.MedicineOrderService;
import com.med.common.result.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/medicineOrder")
public class MedicineOrderController {
    @Resource
    private MedicineOrderService orderService;

    //1 分页查询医嘱
    @GetMapping("/page")
    public Result<IPage<MedicineOrder>> page(@RequestParam(defaultValue = "1") Long pageNum,
                                             @RequestParam(defaultValue = "10") Long pageSize,
                                             @RequestParam(required = false) Long patientId,
                                             HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return orderService.pageOrder(uid, pageNum, pageSize, patientId);
    }
    //2 新增医嘱
    @PostMapping
    public Result<Void> add(@RequestBody MedicineOrder order, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return orderService.addOrder(order, uid);
    }
    //3 修改医嘱
    @PutMapping
    public Result<Void> edit(@RequestBody MedicineOrder order, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return orderService.updateOrder(order, uid);
    }
    //4 删除单条医嘱
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return orderService.deleteOrder(id, uid);
    }
    //5 批量删除医嘱
    @DeleteMapping("/batch")
    public Result<Void> batchDel(@RequestBody List<Long> ids, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return orderService.batchDelete(ids, uid);
    }
    //6 查看详情
    @GetMapping("/{id}")
    public Result<MedicineOrder> getInfo(@PathVariable Long id, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return orderService.getOrderById(id, uid);
    }
    //7 指定患者全部医嘱
    @GetMapping("/patient/{patientId}")
    public Result<List<MedicineOrder>> listByPatient(@PathVariable Long patientId, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return orderService.listByPatient(patientId, uid);
    }
    //8 药品名称去重列表
    @GetMapping("/medicine/distinct")
    public Result<List<String>> distinctMedicine(HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return orderService.distinctMedicineName(uid);
    }
    //9 按月统计医嘱数量
    @GetMapping("/count/month")
    public Result<Long> countMonth(@RequestParam String yearMonth, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return orderService.countByMonth(uid, yearMonth);
    }
    //10 本人医嘱总数
    @GetMapping("/total")
    public Result<Long> total(HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return orderService.totalCount(uid);
    }
    //11 用药提醒列表
    @GetMapping("/remind")
    public Result<List<MedicineOrder>> remind(HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return orderService.remindList(uid);
    }
}