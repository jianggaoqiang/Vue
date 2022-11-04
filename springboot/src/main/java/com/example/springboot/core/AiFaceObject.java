package com.example.springboot.core;

import com.baidu.aip.face.AipFace;
import org.springframework.stereotype.Component;

@Component
public class AiFaceObject {
	public String APP_ID = "26406023";
	public String API_KEY = "2IQVk4IZV8Grk4mmFLGlmyKg";
	public String SECRET_KEY = "1Cxd8Me5aaOAruHtThb8KIMrHyGmMa1W";
	public String GROUD_LIST = "test_bishe";
	    
	private AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);

	// 可选：设置网络连接参数
	public AipFace getClient(){
		client.setConnectionTimeoutInMillis(2000);
		client.setSocketTimeoutInMillis(60000);
		System.out.println("client是"+client);
		return client;
	}
	    
}
