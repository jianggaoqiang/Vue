package com.example.springboot.entity.Vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class ConferenceQuery {

    private  static  final  long serialVersionUID = 1L;
    @ApiModelProperty(value = "会议ID")
    private Integer id;
    @ApiModelProperty(value = "会议名称")
    private String conferenceName;
    @ApiModelProperty(value = "发起人")
    private String initiator;
    @ApiModelProperty(value = "会议开始时间")
    private String beginTime;
    @ApiModelProperty(value = "会议会场")
    private String room;

}
