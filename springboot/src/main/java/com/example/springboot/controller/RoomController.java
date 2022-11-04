package com.example.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.entity.User;
import com.example.springboot.utils.TokenUtils;

import com.example.springboot.service.IRoomService;
import com.example.springboot.entity.Room;

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
@RequestMapping("/room")
public class RoomController {

    @Resource
    private IRoomService roomService;

    private final String now = DateUtil.now();

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Room room) {
        if (room.getId() == null) {
            //room.setTime(DateUtil.now());
            //room.setUser(TokenUtils.getCurrentUser().getUsername());
        }
        roomService.saveOrUpdate(room);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        roomService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        roomService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(roomService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(roomService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam(defaultValue = "") String roomState,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Room> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("room_name", name);
        }
        if (!"".equals(roomState)) {
            queryWrapper.like("room_state", roomState);
        }
        return Result.success(roomService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

//    @ApiOperation(value = "按多条件会议分页查询")
//    @PostMapping ("/page/{pageNum}/{pageSize}")
//    public RData getConference(@ApiParam(name = "pageNum",value = "当前页数",required = true) @PathVariable long pageNum,
//                               @ApiParam(name = "pageSize",value = "每页记录数",required = true) @PathVariable long pageSize,
//                               @ApiParam(name = "conferenceQuery",value = "查询条件") @RequestBody(required = false) ConferenceQuery conferenceQuery){
////        //创建分页对象并获取分页信息
////        conferencePage = new Page<>(pageNum,pageSize);
////        conferenceService.pageQuery(conferencePage,conferenceQuery);
////        //total为总记录数
////        long total = conferencePage.getTotal();
////        //根据页数和每页记录数查询到的数据集合
////        List<Conference> list = conferencePage.getRecords();
////        return RData.rDataSuccess().data("total",total).data("items",list);
//    }



    @GetMapping("/pagebystate")
    public Result findPagebystate(
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Room> queryWrapper = new QueryWrapper<>();
        int state = 1;
        queryWrapper.orderByDesc("id");
        queryWrapper.like("room_state", state);
        return Result.success(roomService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Room> list = roomService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Room信息表", "UTF-8");
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
        List<Room> list = reader.readAll(Room.class);

        roomService.saveBatch(list);
        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

