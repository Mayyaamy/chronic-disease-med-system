package com.med.auth.controller;

import com.med.auth.entity.Patient;
import com.med.auth.service.PatientService;
import com.med.common.result.Result;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/add")
    public Result<String> addPatient(@RequestBody Patient patient) {
        patientService.addPatient(patient);
        return Result.success("患者新增成功");
    }

    @PostMapping("/update")
    public Result<String> updatePatient(@RequestBody Patient patient) {
        patientService.updatePatient(patient);
        return Result.success("患者信息修改成功");
    }

    @DeleteMapping("/{id}")
    public Result<String> deletePatient(@PathVariable(value = "id") Long id) {
        patientService.deletePatient(id);
        return Result.success("患者删除成功");
    }

    @GetMapping("/{id}")
    public Result<Patient> getPatient(@PathVariable(value = "id") Long id) {
        return Result.success(patientService.getPatientById(id));
    }

    @GetMapping("/page")
    public Result<?> pageList(
            @RequestParam(value = "pageNum") Long pageNum,
            @RequestParam(value = "pageSize") Long pageSize,
            @RequestParam(value = "chronicType", required = false) String chronicType
    ) {
        return patientService.patientPage(pageNum, pageSize, chronicType);
    }

    @GetMapping("/count")
    public Result<Long> patientCount() {
        return Result.success(patientService.countMyPatient());
    }

    // ========== 新增7接口 ==========
    @GetMapping("/list/name")
    public Result<List<Patient>> listPatientNameSimple() {
        return Result.success(patientService.listPatientNameSimple());
    }

    @GetMapping("/count/by-type")
    public Result<Long> countByType(@RequestParam(value = "chronicType") String chronicType) {
        return Result.success(patientService.countPatientByType(chronicType));
    }

    @GetMapping("/age/avg")
    public Result<Double> avgAge() {
        return Result.success(patientService.getAvgAge());
    }

    @GetMapping("/oldest")
    public Result<Patient> oldestPatient() {
        return Result.success(patientService.getOldestPatient());
    }

    @GetMapping("/latest")
    public Result<Patient> latestPatient() {
        return Result.success(patientService.getLatestPatient());
    }

    @DeleteMapping("/batch")
    public Result<String> batchDelete(@RequestBody List<Long> ids) {
        patientService.batchDeletePatient(ids);
        return Result.success("批量删除成功");
    }

    @GetMapping("/search/phone")
    public Result<Patient> searchByPhone(@RequestParam(value = "phone") String phone) {
        return Result.success(patientService.searchPatientByPhone(phone));
    }
}