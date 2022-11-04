package com.example.springboot.core;


import com.baidu.aip.face.AipFace;
import com.example.springboot.entity.Image;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class FaceRegistration {
	
	public String Faceregistrtion(AipFace client, String groupList, Image image) throws JSONException {
	    HashMap<String, String> options = new HashMap<String, String>();
	    options.put("user_info", "user's info");
	    options.put("quality_control", "NORMAL");
	    options.put("liveness_control", "NORMAL");
	    JSONObject res = client.addUser(image.getImgStr(), image.getImgType(), groupList, image.getImgId(), options);
		System.err.println(res);
		return res.toString(2);
	}
	
}
