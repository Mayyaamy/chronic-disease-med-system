package com.med.auth.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.med.auth.entity.Patient;
import com.med.auth.service.PatientService;
import com.med.auth.util.JwtUtil;
import com.med.common.result.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Resource
    private PatientService patientService;

    @Resource
    private JwtUtil jwtUtil;

    // 获取当前登录用户ID
    private Long getLoginUserId(HttpServletRequest request) {
        String token = jwtUtil.getTokenFromRequest(request);
        return jwtUtil.getUserIdByToken(token);
    }

    // 新增患者
    @PostMapping("/add")
    public Result<String> add(@RequestBody Patient patient, HttpServletRequest request) {
        patientService.addPatient(patient, getLoginUserId(request));
        return Result.ok("新增患者档案成功");
    }

    // 修改患者
    @PostMapping("/update")
    public Result<String> update(@RequestBody Patient patient, HttpServletRequest request) {
        patientService.updatePatient(patient, getLoginUserId(request));
        return Result.ok("修改患者档案成功");
    }

    // 删除患者
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable(value = "id") Long id, HttpServletRequest request) {
        patientService.deletePatient(id, getLoginUserId(request));
        return Result.ok("删除患者档案成功");
    }

    // 单条详情
    @GetMapping("/{id}")
    public Result<Patient> getInfo(@PathVariable(value = "id") Long id, HttpServletRequest request) {
        Patient patient = patientService.getPatientById(id, getLoginUserId(request));
        return Result.ok(patient);
    }

    // 分页查询本人档案
    @GetMapping("/page")
    public Result<Page<Patient>> page(
            @RequestParam(value = "pageNum", defaultValue = "1") Long pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Long pageSize,
            HttpServletRequest request
    ) {
        Page<Patient> page = patientService.pagePatient(getLoginUserId(request), pageNum, pageSize);
        return Result.ok(page);
    }

    // 分页入参封装（表单提交可用，JSON用上面@RequestBody即可）
    @Data
    public static class PageParam {
        private Long pageNum;
        private Long pageSize;
    }
}