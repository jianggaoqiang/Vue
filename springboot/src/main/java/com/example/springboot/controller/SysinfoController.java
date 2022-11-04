package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.config.interceptor.AuthAccess;
import com.example.springboot.entity.Conference;
import com.example.springboot.entity.ConferenceDetail;
import com.example.springboot.entity.User;
import com.example.springboot.service.IConferenceDetailService;
import com.example.springboot.service.IConferenceService;
import com.example.springboot.service.IUserService;
import com.example.springboot.utils.RData;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/sysinfo")
public class SysinfoController {

    @Resource
    IUserService userService;

    @Resource
    IConferenceService conferenceService;

    @Resource
    IConferenceDetailService conferenceDetailService;


    @AuthAccess
    @ApiOperation(value = "用户数量以及管理员数量")
    @GetMapping("findAll")
    public RData findAll(){
        List<User> list = userService.list();
        long total = list.size();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role","ROLE_ADMIN");
        List<User> list1 = userService.list(queryWrapper);
        long admin = list1.size();
        List<Conference> list3 = conferenceService.list();
        long conferencelist = list3.size();
        List<ConferenceDetail> list4 = conferenceDetailService.list();
        long conferenceDetailList = list4.size();
        return RData.rDataSuccess().data("total",total).data("admin",admin).data("conferenceList",conferencelist).data("conferenceDetailList",conferenceDetailList);

    }


    @AuthAccess
    @ApiOperation(value = "用户角色饼图")
    @GetMapping("pieAll")
    public RData pieAll(){
        List<User> list = userService.list();
        long total = list.size();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role","ROLE_ADMIN");
        List<User> list1 = userService.list(queryWrapper);
        long admin = list1.size();
        long putong = total-admin;
        return RData.rDataSuccess().data("putong",putong).data("admin",admin);
    }









}
