package com.example.springboot.controller;



import com.baidu.aip.face.AipFace;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.config.interceptor.AuthAccess;
import com.example.springboot.core.AiFaceObject;
import com.example.springboot.core.FaceUser;
import com.example.springboot.entity.ConferenceDetail;
import com.example.springboot.entity.Image;
import com.example.springboot.entity.ImageResult;
import com.example.springboot.entity.User;
import com.example.springboot.service.FaceRegisterService;
import com.example.springboot.service.IConferenceDetailService;
import com.example.springboot.service.IUserService;
import com.example.springboot.utils.RData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/face")
public class FaceController {

    @Autowired
    FaceRegisterService faceRegisterService;

    @Autowired
    AiFaceObject aiFaceObject;

    @Resource
    private IConferenceDetailService conferenceDetailService;

    @Resource
    private    IUserService userService;
    @Autowired
    FaceUser faceUser;


    @AuthAccess
    @RequestMapping("/register")
    @ResponseBody
    public ImageResult register(Image image ,Integer userId) throws JSONException {
        ImageResult ImageResult = faceRegisterService.register(image);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User user = userService.getOne(queryWrapper.eq("id", userId));
        user.setFace(1);
        userService.updateById(user);
        return ImageResult;
    }


    @AuthAccess
    @PostMapping ("/sign")
    @ResponseBody
    public RData sign(Image image ,Integer conferenceDetailId) throws JSONException {
        AipFace client= aiFaceObject.getClient();
        String res=faceUser.faceuser(client, image);
        JSONObject ImageResult = new JSONObject(res);
        String errorMsg = ImageResult.getString("error_msg");
        if(errorMsg == "face is fuzzy"){
            return RData.rDataError().message(errorMsg);
        }
        JSONArray faceList = ImageResult.getJSONObject("result").getJSONArray("user_list");
//        List<FaceResult> list =  com.alibaba.fastjson.JSONObject.parseArray(faceList.toString(), FaceResult.class);
        if ("SUCCESS".equals(errorMsg)) {//成功
            LocalDateTime localDate = LocalDateTime.now();
            QueryWrapper<ConferenceDetail>  queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",conferenceDetailId);
            ConferenceDetail one = conferenceDetailService.getOne(queryWrapper);
            one.setSign(1);
            one.setSignTime(localDate);
            conferenceDetailService.updateById(one);
            return RData.rDataSuccess().message(faceList.toString());
        }else {
            System.out.println("errorMsg==============================="+errorMsg);
            return RData.rDataError().message(faceList.toString());
        }
    }














}
