package com.med.auth.controller;

import com.med.auth.entity.PatientFollow;
import com.med.auth.service.PatientFollowService;
import com.med.common.result.Result;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/follow")
public class PatientFollowController {

    private final PatientFollowService followService;

    public PatientFollowController(PatientFollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/add")
    public Result<String> addFollow(@RequestBody PatientFollow follow) {
        followService.addFollow(follow);
        return Result.success("随访新增成功");
    }

    @PostMapping("/update")
    public Result<String> updateFollow(@RequestBody PatientFollow follow) {
        followService.updateFollow(follow);
        return Result.success("随访修改成功");
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteFollow(@PathVariable(value = "id") Long id) {
        followService.deleteFollow(id);
        return Result.success("随访删除成功");
    }

    @GetMapping("/{id}")
    public Result<PatientFollow> getFollow(@PathVariable(value = "id") Long id) {
        return Result.success(followService.getFollowById(id));
    }

    @GetMapping("/page")
    public Result<?> pageFollow(
            @RequestParam(value = "patientId") Long patientId,
            @RequestParam(value = "pageNum") Long pageNum,
            @RequestParam(value = "pageSize") Long pageSize
    ) {
        return followService.followPage(patientId, pageNum, pageSize);
    }

    @GetMapping("/today/pending")
    public Result<?> todayPending() {
        return followService.todayPendingList();
    }

    @GetMapping("/overdue/list")
    public Result<?> overdueList() {
        return followService.overdueList();
    }

    @PutMapping("/finish")
    public Result<String> finishFollow(
            @RequestParam(value = "followId") Long followId
    ) {
        followService.finishFollow(followId);
        return Result.success("随访标记完成");
    }

    // ========== 新增6接口 ==========
    @GetMapping("/count/total")
    public Result<Long> countTotal() {
        return Result.success(followService.countTotalFollow());
    }

    @GetMapping("/count/finish")
    public Result<Long> countFinish() {
        return Result.success(followService.countFinishFollow());
    }

    @GetMapping("/count/unfinish")
    public Result<Long> countUnfinish() {
        return Result.success(followService.countUnfinishFollow());
    }

    @GetMapping("/month/list")
    public Result<?> monthList(@RequestParam(value = "yearMonth") String yearMonth) {
        return followService.monthFollowList(yearMonth);
    }

    @DeleteMapping("/batch")
    public Result<String> batchDelete(@RequestBody List<Long> ids) {
        followService.batchDeleteFollow(ids);
        return Result.success("批量删除成功");
    }

    @GetMapping("/latest/{patientId}")
    public Result<PatientFollow> latestFollow(@PathVariable(value = "patientId") Long patientId) {
        return Result.success(followService.getLatestFollowByPatient(patientId));
    }
}