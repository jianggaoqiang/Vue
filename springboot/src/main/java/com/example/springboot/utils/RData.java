package com.example.springboot.utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class RData {
    @ApiModelProperty(value = "是否成功")
    private Boolean success;
    @ApiModelProperty(value = "返回状态码")
    private Integer code;
    @ApiModelProperty(value = "返回消息")
    private String message;
    @ApiModelProperty(value = "返回数据")
    private Map<String,Object> data = new HashMap<>();
    public static RData rDataSuccess(){
        RData rData = new RData();
        rData.setSuccess(true);
        rData.setCode(ResultCode.SUCCESS);
        rData.setMessage("成功");
        return rData;
    }
    public static RData rDataError(){
        RData rData = new RData();
        rData.setSuccess(false);
        rData.setCode(ResultCode.ERROR);
        rData.setMessage("失败");
        return rData;
    }
    public RData success(Boolean success){
        this.setSuccess(success);
        return this;
    }
    public RData message(String message){
        this.setMessage(message);
        return this;
    }
    public RData code(Integer code){
        this.setCode(code);
        return this;
    }
    public RData data(String key, Object value){
        this.data.put(key, value);
        return this;
    }
    public RData data(Map<String,Object> map){
        this.setData(map);
        return this;
    }
}
