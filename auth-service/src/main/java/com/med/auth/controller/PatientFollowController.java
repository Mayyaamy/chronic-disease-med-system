package com.med.auth.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.med.auth.entity.PatientFollow;
import com.med.auth.service.PatientFollowService;
import com.med.auth.util.JwtUtil;
import com.med.common.result.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follow")
public class PatientFollowController {

    @Resource
    private PatientFollowService followService;

    @Resource
    private JwtUtil jwtUtil;

    // 获取当前登录用户ID
    private Long getLoginUserId(HttpServletRequest request) {
        String token = jwtUtil.getTokenFromRequest(request);
        return jwtUtil.getUserIdByToken(token);
    }

    // 新增随访记录
    @PostMapping("/add")
    public Result<String> add(@RequestBody PatientFollow follow, HttpServletRequest request) {
        followService.addFollow(follow, getLoginUserId(request));
        return Result.ok("新增随访记录成功");
    }

    // 修改随访记录
    @PostMapping("/update")
    public Result<String> update(@RequestBody PatientFollow follow, HttpServletRequest request) {
        followService.updateFollow(follow, getLoginUserId(request));
        return Result.ok("修改随访记录成功");
    }

    // 删除随访记录
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable(value = "id") Long id, HttpServletRequest request) {
        followService.deleteFollow(id, getLoginUserId(request));
        return Result.ok("删除随访记录成功");
    }

    // 单条随访详情
    @GetMapping("/{id}")
    public Result<PatientFollow> getInfo(@PathVariable(value = "id") Long id, HttpServletRequest request) {
        PatientFollow follow = followService.getFollowById(id, getLoginUserId(request));
        return Result.ok(follow);
    }

    // 分页查询某个患者的所有随访
    @GetMapping("/page")
    public Result<Page<PatientFollow>> page(
            @RequestParam(value = "patientId") Long patientId,
            @RequestParam(value = "pageNum", defaultValue = "1") Long pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Long pageSize,
            HttpServletRequest request
    ) {
        Page<PatientFollow> page = followService.pageFollowByPatient(patientId, getLoginUserId(request), pageNum, pageSize);
        return Result.ok(page);
    }
}