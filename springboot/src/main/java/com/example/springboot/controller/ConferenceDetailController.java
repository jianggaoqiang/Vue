package com.example.springboot.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Constants;
import com.example.springboot.config.interceptor.AuthAccess;
import com.example.springboot.entity.*;
import com.example.springboot.entity.Vo.ConferenceDetailList;
import com.example.springboot.entity.Vo.ConferenceDetailQuery;
import com.example.springboot.entity.Vo.UserDetail;
import com.example.springboot.service.IConferenceService;
import com.example.springboot.service.IUserService;
import com.example.springboot.utils.RData;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.utils.TokenUtils;

import com.example.springboot.service.IConferenceDetailService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 蒋高强
 * @since 2022-10-12
 */
@RestController
@RequestMapping("/conferenceDetail")
public class ConferenceDetailController {

    @Resource
    private IConferenceDetailService conferenceDetailService;

    @Resource
    private IConferenceService conferenceService;

    @Resource
    private IUserService userService;

    private Page<ConferenceDetail> conferenceDetailPage = null;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody ConferenceDetail conferenceDetail) {
        if (conferenceDetail.getId() == null) {

        }
        Integer conferenceId = conferenceDetail.getConferenceId();
        QueryWrapper<Conference> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",conferenceId);

        LocalDateTime localDateTime = LocalDateTime.now();
        Conference one = conferenceService.getOne(queryWrapper);
        System.err.println(one);
        if(one != null){
            LocalDateTime endTime = one.getEndTime();
            if (localDateTime.isAfter(endTime)){
                return Result.error(Constants.CODE_400,"会议已结束");
            }
            conferenceDetailService.saveOrUpdate(conferenceDetail);
            QueryWrapper<ConferenceDetail> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("conference_id",conferenceId);
            List<ConferenceDetail> list = conferenceDetailService.list(queryWrapper1);
            Integer total = list.size();
            queryWrapper1.eq("sign",1);
            List<ConferenceDetail> list1 = conferenceDetailService.list(queryWrapper1);
            Integer signtotal = list1.size();
            one.setJoinNumber(total);
            one.setSignNumber(signtotal);
            conferenceService.updateById(one);
            return Result.success();
        }else {
            return Result.error(Constants.CODE_400,"该会议号不存在");
        }
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        conferenceDetailService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        conferenceDetailService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(conferenceDetailService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(conferenceDetailService.getById(id));
    }




    @AuthAccess
    @ApiOperation(value = "按多条件会议分页查询")
    @PostMapping ("/page/{pageNum}/{pageSize}")
    public RData getConference(@ApiParam(name = "pageNum",value = "当前页数",required = true) @PathVariable long pageNum,
                               @ApiParam(name = "pageSize",value = "每页记录数",required = true) @PathVariable long pageSize,
                               @ApiParam(name = "conferenceQuery",value = "查询条件") @RequestBody(required = false) ConferenceDetailQuery conferenceDetailQuery){
        //创建分页对象并获取分页信息
        conferenceDetailPage = new Page<>(pageNum,pageSize);
        conferenceDetailService.pageQuery(conferenceDetailPage,conferenceDetailQuery);
        //total为总记录数
        long total = conferenceDetailPage.getTotal();
        //根据页数和每页记录数查询到的数据集合
        List<ConferenceDetail> list = conferenceDetailPage.getRecords();
        return RData.rDataSuccess().data("total",total).data("items",list);
    }


    @AuthAccess
    @ApiOperation(value = "参加的会议信息(还没召开的会议)")
    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") Integer userId,
                           @RequestParam(defaultValue = "") Integer sign,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<ConferenceDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("join_time");
        if (!"".equals(userId)) {
            queryWrapper.eq("user_id", userId);
        }
        if (!"".equals(sign)) {
            queryWrapper.eq("sign", sign);
        }
        List<ConferenceDetail> list = conferenceDetailService.list(queryWrapper);
        List<ConferenceDetailList> lists = new ArrayList<>();
        for(ConferenceDetail i : list){
            Integer conferenceId = i.getConferenceId();

            Conference conference = conferenceService.getById(conferenceId);

            ConferenceDetailList conferenceDetailList = new ConferenceDetailList();
            if(conference!=null){
                BeanUtils.copyProperties(conference,conferenceDetailList);
            }
            if(i != null){
                BeanUtils.copyProperties(i,conferenceDetailList);
            }
            conferenceDetailList.setId(i.getId());
            conferenceDetailList.setConferenceId(conferenceId);
            lists.add(conferenceDetailList);

        }
        return Result.success(lists);

    }

    @AuthAccess
    @ApiOperation(value = "获取参加会议的用户信息")
    @GetMapping("/conferenceuserdetail/{conferenceId}")
    public RData conferenceDetail(@RequestParam Integer conferenceId) {
        QueryWrapper<ConferenceDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("join_time");
        if (!"".equals(conferenceId)) {
            queryWrapper.eq("conference_id", conferenceId);
        }
        List<ConferenceDetail> list = conferenceDetailService.list(queryWrapper);
        queryWrapper.eq("sign",1);
        List<ConferenceDetail> list2 = conferenceDetailService.list(queryWrapper);
        long signTotal = list2.size();
        List<UserDetail> lists = new ArrayList<>();
        for(ConferenceDetail i : list){
            Integer user = i.getUserId();
            User user1 = userService.getById(user);
            UserDetail userDetail = new UserDetail();
            if(i != null){
                BeanUtils.copyProperties(i,userDetail);
            }
            if(user1!=null){
                BeanUtils.copyProperties(user1,userDetail);
            }
            userDetail.setConferenceDetailId(i.getId());
            lists.add(userDetail);
        }
        return RData.rDataSuccess().data("total",signTotal).data("items",lists);
//        return Result.success(lists);

    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<ConferenceDetail> list = conferenceDetailService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("ConferenceDetail信息表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

        }

    /**
     * excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<ConferenceDetail> list = reader.readAll(ConferenceDetail.class);

        conferenceDetailService.saveBatch(list);
        return Result.success();
    }

    /**
     * 代签
     * @param conferenceDetailId
     * @return
     */

    @PutMapping("/shoudongsign/{conferenceDetailId}")
    public Result sign(@PathVariable Integer conferenceDetailId){
        QueryWrapper<ConferenceDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",conferenceDetailId);
        ConferenceDetail one = conferenceDetailService.getOne(queryWrapper);
        if(one.getSign() == 1){
            return Result.success("该用户已签到");
        }
        LocalDateTime localDate = LocalDateTime.now();
        one.setSign(1);
        one.setSignTime(localDate);
        conferenceDetailService.updateById(one);
        return Result.success(one);
    }


}

