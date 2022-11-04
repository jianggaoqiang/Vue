package com.example.springboot.service.impl;



import com.baidu.aip.face.AipFace;

import com.example.springboot.core.AiFaceObject;
import com.example.springboot.core.FaceRegistration;
import com.example.springboot.core.FaceUser;
import com.example.springboot.entity.Image;
import com.example.springboot.entity.ImageResult;
import com.example.springboot.service.FaceRegisterService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FaceRegisterServiceImp implements FaceRegisterService {
    @Autowired
    AiFaceObject aiFaceObject;
    @Autowired
    FaceRegistration faceRegistration;
    @Autowired
    FaceUser faceUser;
    @Override
    public ImageResult register(Image image) throws JSONException {
        ImageResult message = new ImageResult();
        if(!search(image)) {
            JSONObject ImageResult = faceRegister(image,aiFaceObject.GROUD_LIST);
            int error_code = ImageResult.getInt("error_code");
            if (error_code == 0){//注册成功
                message.setStart(true);
                message.setMsg("注册成功");
            }else if (error_code==222202){
                message.setStart(false);
                message.setErrorMsg("请将脸部对准摄像头");
            }else {
                message.setStart(false);
                message.setErrorMsg("错误代码"+ImageResult.getInt("error_code"));
            }
        }else {
            message.setStart(false);
            message.setErrorMsg("人脸数据已经被注册");
        }
        return message;
    }

    private JSONObject faceRegister(Image image,String groudList) throws JSONException {
        AipFace client= aiFaceObject.getClient();
        String res = faceRegistration.Faceregistrtion(client,groudList,image);
        JSONObject ImageResult = new JSONObject(res);
        return ImageResult;
    }


    //在人脸库搜索人脸
    public boolean search(Image image) throws JSONException {
        AipFace client= aiFaceObject.getClient();
        String res=faceUser.faceuser(client, image);
        JSONObject ImageResult = new JSONObject(res);
        String errorMsg = ImageResult.getString("error_msg");
        if ("SUCCESS".equals(errorMsg)){//成功
            JSONArray faceList = ImageResult.getJSONObject("result").getJSONArray("user_list");
            JSONObject user = faceList.getJSONObject(0);
            double score = user.getDouble("score");
            System.out.println("==============您的分数是============"+score);
            if (score > 80){//匹配相似度80以上
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
