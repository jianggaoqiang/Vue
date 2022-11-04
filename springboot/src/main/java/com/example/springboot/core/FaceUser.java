package com.example.springboot.core;

import com.baidu.aip.face.AipFace;
import com.example.springboot.entity.Image;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class FaceUser {
	@Autowired
	AiFaceObject aiFaceObject;
	public String faceuser(AipFace client, Image image) throws JSONException {
	    HashMap<String, String> options = new HashMap<String, String>();
	    options.put("quality_control", "LOW");
	    options.put("liveness_control", "LOW");
	    options.put("max_user_num", "1");

	    String groupIdList = aiFaceObject.GROUD_LIST;//人脸库组名称（需要自己修改）
	    JSONObject res = client.search(image.getImgStr(), image.getImgType(), groupIdList, options);
	    return res.toString(2);
	}
}
