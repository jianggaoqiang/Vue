package com.example.springboot;

import com.baidu.aip.face.AipFace;
import com.baidu.aip.util.Base64Util;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

@SpringBootTest
class SpringbootApplicationTests {
    //设置APPID/AK/SK
    public static final String APP_ID = "26406023";
    public static final String API_KEY = "2IQVk4IZV8Grk4mmFLGlmyKg";
    public static final String SECRET_KEY = "1Cxd8Me5aaOAruHtThb8KIMrHyGmMa1W";

    public static void main(String[] args) throws Exception {
        // 初始化一个AipFace
        AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);

        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("quality_control", "LOW");
        options.put("liveness_control", "LOW");

        // 调用接口
        String path = "C:\\Users\\jgq20\\Pictures\\憨强\\蒋高强.jpg"; //电脑本地的一张图片
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String image = Base64Util.encode(bytes);
        String imageType = "BASE64";
        String groupId = "1001"; //用户组id
        String userId = "user1"; //用户id

        // 人脸注册
        JSONObject res = client.addUser(image,imageType,groupId,userId,options);
        System.out.println(res.toString(2));
    }



//    @Test
//    void contextLoads() {
//        AuthService authService = new AuthService();
//        String res=  authService.getAuth();
//        System.out.println(res+"jianggaoqiang ");
//    }
//    24.87ae13d051ca2c77a8c1dda2334df320.2592000.1666486555.282335-26406023
//    24.0a3f5b05c6e1b6ed8efafae543ff03ab.2592000.1666492847.282335-26406023
//



}
