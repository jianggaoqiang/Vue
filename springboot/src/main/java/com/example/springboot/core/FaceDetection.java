package com.example.springboot.core;


import com.baidu.aip.face.AipFace;
import com.example.springboot.entity.Image;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class FaceDetection {

	public String Facedetection(AipFace client, Image image) throws JSONException {
		HashMap<String, String> options= new HashMap<String, String>();
		options.put("face_field", "age");
		options.put("max_face_num", "1");
		options.put("face_type", "LIVE");

		JSONObject res=client.detect(image.getImgStr(), image.getImgType(), options);
		System.out.println(res);
		return res.toString(2);
	}

	
}
