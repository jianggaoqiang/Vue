package com.example.springboot.service.impl;


import com.baidu.aip.face.AipFace;
import com.example.springboot.core.AiFaceObject;
import com.example.springboot.core.FaceDetection;
import com.example.springboot.core.FaceUser;
import com.example.springboot.entity.Image;
import com.example.springboot.entity.ImageResult;
import com.example.springboot.service.FaceSearchService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FaceSearchServiceImp implements FaceSearchService {
    @Autowired
    AiFaceObject aiFaceObject;
    @Autowired
    FaceDetection faceDetection;
    @Autowired
    FaceUser faceUser;

    @Override
    public ImageResult faceLogin(Image image) throws JSONException {
        ImageResult detection = DetectionFace(image);
        System.out.println("detection是"+detection);
        if (detection.isStart()){
            ImageResult search = search(image);
            if (search.isStart()){
                return search;
            }else {
                ImageResult result = new ImageResult();
                result.setStart(false);
                result.setErrorMsg("匹配不成功");
                return result;
            }
        }else {
            ImageResult result = new ImageResult();
            result.setStart(false);
            result.setErrorMsg("人脸不合格");
            return result;
        }
    }

    //检测人脸
    private ImageResult DetectionFace(Image image) throws JSONException {
        ImageResult message = new ImageResult();
        AipFace client= aiFaceObject.getClient();
        String res=faceDetection.Facedetection(client,image);
        JSONObject result = new JSONObject(res);
        String errorMsg = result.getString("error_msg");
        if ("SUCCESS".equals(errorMsg)){//成功
            result = result.getJSONObject("result");
            int faceNum = result.getInt("face_num");
            JSONArray faceList = result.getJSONArray("face_list");
            JSONObject subObj = faceList.getJSONObject(0);
            double faceProbability = subObj.getDouble("face_probability");
            if (faceNum==1 && faceProbability > 0.8){//登录的人唯一，是人的准确率0.8以上
                message.setStart(true);
                message.setFaceNum(faceNum);
                message.setMsg("成功");
            }else {
                message.setStart(false);
                message.setFaceNum(faceNum);
                message.setErrorMsg("图片不能满足登录要求");
            }
        }else {
            message.setStart(false);
            message.setFaceNum(0);
            message.setErrorMsg("匹配库不成功");
        }
        return message;
    }

    //在人脸库搜索人脸
    private ImageResult search(Image image) throws JSONException {
        ImageResult message = new ImageResult();
        AipFace client= aiFaceObject.getClient();
        String res=faceUser.faceuser(client, image);
        JSONObject result = new JSONObject(res);
        String errorMsg = result.getString("error_msg");
        if ("SUCCESS".equals(errorMsg)){//成功
            JSONArray faceList = result.getJSONObject("result").getJSONArray("user_list");
            JSONObject user = faceList.getJSONObject(0);
            double score = user.getDouble("score");
            String userId = user.getString("user_id");
            if (score > 80){//匹配相似度80以上
                message.setStart(true);
                message.setMsg("成功");
                message.setUserId(userId);
            }else {
                message.setStart(false);
                message.setErrorMsg("相似度过低");
            }
        }else {
            message.setStart(false);
            message.setErrorMsg("人脸库查询不成功");
        }
        return message;
    }
}
