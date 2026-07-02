package com.med.auth.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.med.auth.entity.Patient;
import com.med.auth.service.PatientService;
import com.med.common.result.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {
    @Resource
    private PatientService patientService;

    //1 分页查询患者
    @GetMapping("/page")
    public Result<IPage<Patient>> page(@RequestParam(defaultValue = "1") Long pageNum,
                                       @RequestParam(defaultValue = "10") Long pageSize,
                                       @RequestParam(required = false) String keyword,
                                       HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return patientService.pagePatient(uid, pageNum, pageSize, keyword);
    }
    //2 新增患者
    @PostMapping
    public Result<Void> add(@RequestBody Patient patient, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return patientService.addPatient(patient, uid);
    }
    //3 修改患者
    @PutMapping
    public Result<Void> edit(@RequestBody Patient patient, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return patientService.updatePatient(patient, uid);
    }
    //4 删除单个
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return patientService.deletePatient(id, uid);
    }
    //5 批量删除
    @DeleteMapping("/batch")
    public Result<Void> batchDel(@RequestBody List<Long> ids, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return patientService.batchDelete(ids, uid);
    }
    //6 根据ID查询详情
    @GetMapping("/{id}")
    public Result<Patient> getInfo(@PathVariable Long id, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return patientService.getPatientById(id, uid);
    }
    //7 按慢病类型统计数量
    @GetMapping("/count/type")
    public Result<Long> countType(@RequestParam String type, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return patientService.countByChronicType(uid, type);
    }
    //8 患者名下拉列表
    @GetMapping("/name/list")
    public Result<List<Patient>> nameList(HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return patientService.getNameList(uid);
    }
    //9 平均年龄统计
    @GetMapping("/avg/age")
    public Result<Double> avgAge(HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return patientService.getAvgAge(uid);
    }
    //10 年龄最大患者
    @GetMapping("/max/age")
    public Result<Patient> maxAge(HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return patientService.getMaxAgePatient(uid);
    }
    //11 最新创建患者
    @GetMapping("/latest")
    public Result<Patient> latest(HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return patientService.getLatestPatient(uid);
    }
    //12 手机号模糊检索
    @GetMapping("/search/phone")
    public Result<List<Patient>> searchPhone(@RequestParam String phone, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return patientService.searchByPhone(uid, phone);
    }
    //13 总患者统计
    @GetMapping("/total")
    public Result<Long> total(HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return patientService.totalCount(uid);
    }
}