package com.example.springboot.entity.Vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class UserDetail {

    @ApiModelProperty("会议记录详情id")
    private Integer conferenceDetailId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("头像")
    private String avatarUrl;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("签到")
    private Integer sign;

    @ApiModelProperty("签到时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime signTime;


    @ApiModelProperty("预定会议时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime joinTime;





}
