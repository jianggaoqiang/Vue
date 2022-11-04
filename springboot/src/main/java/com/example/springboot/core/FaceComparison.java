package com.example.springboot.core;


import com.baidu.aip.face.AipFace;
import com.baidu.aip.face.MatchRequest;
import com.example.springboot.entity.Image;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class FaceComparison {
	
	public String Facecomparison(AipFace client, Image imageU, Image imageC) throws JSONException {
		System.out.println("三个参数是：");
		System.out.println(client);
		System.out.println(imageU);
		System.out.println(imageC);
		MatchRequest req1 = new MatchRequest(imageU.getImgStr(), imageU.getImgType());
	    MatchRequest req2 = new MatchRequest(imageC.getImgStr(), imageC.getImgType());
	    ArrayList<MatchRequest> requests = new ArrayList<MatchRequest>();
	    requests.add(req1);
	    requests.add(req2);
	    JSONObject res = client.match(requests);
		return res.toString(2);
	}

}
