package com.example.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Constants;
import com.example.springboot.config.interceptor.AuthAccess;
import com.example.springboot.entity.ConferenceDetail;
import com.example.springboot.entity.Room;
import com.example.springboot.entity.Vo.ConferenceQuery;
import com.example.springboot.service.IConferenceDetailService;
import com.example.springboot.service.IRoomService;
import com.example.springboot.utils.RData;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.entity.User;
import com.example.springboot.utils.TokenUtils;

import com.example.springboot.service.IConferenceService;
import com.example.springboot.entity.Conference;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 蒋高强
 * @since 2022-10-10
 */
@RestController
@RequestMapping("/conference")
public class ConferenceController {

    @Autowired
    private IConferenceService conferenceService;

    @Autowired
    IConferenceDetailService conferenceDetailService;

    @Resource
    private IRoomService roomService;

    private Page<Conference> conferencePage = null;


    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Conference conference) {
        if (conference.getId() == null) {
            //conference.setTime(DateUtil.now());
            //conference.setUser(TokenUtils.getCurrentUser().getUsername());
        }
        QueryWrapper<Conference> queryWrapper = new QueryWrapper<>();
        List<Conference> list = conferenceService.list(queryWrapper.eq("room", conference.getRoom()));
        for(Conference i:list){
            LocalDateTime beginTime = i.getBeginTime();
            LocalDateTime endTime = i.getEndTime();
            //1
            if(conference.getBeginTime().isBefore(beginTime)&& conference.getEndTime().isAfter(beginTime)){
                return  Result.error(Constants.CODE_500,"该会议室时间冲突，请重新选择时间或房间0");
            }

            //2
            if(conference.getBeginTime().isAfter(beginTime) && conference.getBeginTime().isBefore(endTime) ){
                return  Result.error(Constants.CODE_500,"该会议室时间冲突，请重新选择时间或房间1");
            }
            //包围4
            if(conference.getBeginTime().isBefore(beginTime) && conference.getBeginTime().isAfter(endTime)){
                return  Result.error(Constants.CODE_500,"该会议室时间冲突，请重新选择时间或房间2");
            }

            //3
            if(conference.getBeginTime().isAfter(beginTime) && conference.getBeginTime().isBefore(endTime)){
                return  Result.error(Constants.CODE_500,"该会议室时间冲突，请重新选择时间或房间4");
            }

            if(conference.getBeginTime().equals(beginTime) && conference.getEndTime().equals(endTime)){
                return  Result.error(Constants.CODE_500,"该会议室时间冲突，请重新选择时间或房间5");
            }

        }





        conferenceService.saveOrUpdate(conference);


//        String roomName = conference.getRoom();
//        QueryWrapper<Room> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("room_name", roomName);
//        Room room = roomService.getOne(queryWrapper);
//        room.setRoomState(0);
//        roomService.updateById(room);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        conferenceService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        conferenceService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(conferenceService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(conferenceService.getById(id));
    }



    @AuthAccess
    @ApiOperation(value = "按多条件会议分页查询")
    @PostMapping ("/page/{pageNum}/{pageSize}")
    public RData getConference(@ApiParam(name = "pageNum",value = "当前页数",required = true) @PathVariable long pageNum,
                                      @ApiParam(name = "pageSize",value = "每页记录数",required = true) @PathVariable long pageSize,
                                      @ApiParam(name = "conferenceQuery",value = "查询条件") @RequestBody(required = false) ConferenceQuery conferenceQuery){
        //创建分页对象并获取分页信息
        conferencePage = new Page<>(pageNum,pageSize);
        conferenceService.pageQuery(conferencePage,conferenceQuery);
        //total为总记录数
        long total = conferencePage.getTotal();
        //根据页数和每页记录数查询到的数据集合
        List<Conference> list = conferencePage.getRecords();
        return RData.rDataSuccess().data("total",total).data("items",list);
    }

    /**
    * 所有会议导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Conference> list = conferenceService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Conference信息表", "UTF-8");
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
        List<Conference> list = reader.readAll(Conference.class);

        conferenceService.saveBatch(list);
        return Result.success();
    }


    @ApiOperation(value = "查询已经结束的会议")
    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String endTime,
                           @RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Conference> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(endTime)) {
            queryWrapper.le("end_time", endTime);
        }
        if(!"".equals(name)){
            queryWrapper.eq("initiator",name);
        }
        return Result.success(conferenceService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }




}

