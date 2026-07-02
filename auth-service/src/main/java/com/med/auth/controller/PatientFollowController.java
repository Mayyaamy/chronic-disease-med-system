package com.med.auth.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.med.auth.entity.PatientFollow;
import com.med.auth.service.PatientFollowService;
import com.med.common.result.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/follow")
public class PatientFollowController {
    @Resource
    private PatientFollowService followService;

    //1 分页查询随访
    @GetMapping("/page")
    public Result<IPage<PatientFollow>> page(@RequestParam(defaultValue = "1") Long pageNum,
                                             @RequestParam(defaultValue = "10") Long pageSize,
                                             @RequestParam(required = false) Long patientId,
                                             HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return followService.pageFollow(uid, pageNum, pageSize, patientId);
    }
    //2 新增随访
    @PostMapping
    public Result<Void> add(@RequestBody PatientFollow follow, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return followService.addFollow(follow, uid);
    }
    //3 修改随访
    @PutMapping
    public Result<Void> edit(@RequestBody PatientFollow follow, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return followService.updateFollow(follow, uid);
    }
    //4 删除单条随访
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return followService.deleteFollow(id, uid);
    }
    //5 批量删除随访
    @DeleteMapping("/batch")
    public Result<Void> batchDel(@RequestBody List<Long> ids, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return followService.batchDelete(ids, uid);
    }
    //6 查看单条详情
    @GetMapping("/{id}")
    public Result<PatientFollow> getInfo(@PathVariable Long id, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return followService.getFollowById(id, uid);
    }
    //7 待完成随访列表
    @GetMapping("/pending")
    public Result<List<PatientFollow>> pendingList(HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return followService.getPendingList(uid);
    }
    //8 已完成随访列表
    @GetMapping("/finished")
    public Result<List<PatientFollow>> finishedList(HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return followService.getFinishedList(uid);
    }
    //9 按月统计随访数量
    @GetMapping("/count/month")
    public Result<Long> countMonth(@RequestParam String yearMonth, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return followService.countByMonth(uid, yearMonth);
    }
    //10 标记随访完成
    @PutMapping("/finish/{id}")
    public Result<Void> finish(@PathVariable Long id, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return followService.finishFollow(id, uid);
    }
    //11 指定患者全部随访
    @GetMapping("/patient/{patientId}")
    public Result<List<PatientFollow>> listByPatient(@PathVariable Long patientId, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return followService.listByPatient(patientId, uid);
    }
    //12 患者最近一条随访
    @GetMapping("/latest/{patientId}")
    public Result<PatientFollow> latest(@PathVariable Long patientId, HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return followService.latestFollow(patientId, uid);
    }
    //13 本人总随访条数
    @GetMapping("/total")
    public Result<Long> totalCount(HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return followService.totalFollowCount(uid);
    }
    //14 未完成随访统计
    @GetMapping("/pending/count")
    public Result<Long> pendingCount(HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("currentUserId");
        return followService.pendingFollowCount(uid);
    }
}