package com.example.springboot.entity.Vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ConferenceDetailQuery {

    private  static  final  long serialVersionUID = 1L;
    @ApiModelProperty(value = "会议ID")
    private Integer conferenceId;
    @ApiModelProperty(value = "用户ID")
    private Integer userId;
    @ApiModelProperty(value = "加入会议时间")
    private String joinTime;
    @ApiModelProperty(value = "是否签到")
    private Integer sign;

}
