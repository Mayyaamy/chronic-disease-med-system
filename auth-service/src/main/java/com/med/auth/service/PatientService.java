package com.med.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.med.auth.entity.Patient;
import com.med.common.result.Result;
import java.util.List;

public interface PatientService extends IService<Patient> {
    void addPatient(Patient patient);
    void updatePatient(Patient patient);
    void deletePatient(Long id);
    Patient getPatientById(Long id);
    Result<?> patientPage(Long pageNum, Long pageSize, String chronicType);
    Long countMyPatient();
    void checkPatientOwner(Long patientId);

    // 新增7个方法
    List<Patient> listPatientNameSimple();
    Long countPatientByType(String chronicType);
    Double getAvgAge();
    Patient getOldestPatient();
    Patient getLatestPatient();
    void batchDeletePatient(List<Long> ids);
    Patient searchPatientByPhone(String phone);
}