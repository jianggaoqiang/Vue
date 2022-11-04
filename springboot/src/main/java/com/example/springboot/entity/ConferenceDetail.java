package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 蒋高强
 * @since 2022-10-12
 */

@Getter
@Setter
@TableName("sys_conference_detail")
@ApiModel(value = "ConferenceDetail对象", description = "")
public class ConferenceDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("会议ID")
    private Integer conferenceId;

    @ApiModelProperty("用户ID")
    private Integer userId;



    @ApiModelProperty("签到（0是未签，1是签到）")
    private Integer sign;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty("签到时间")
    private LocalDateTime signTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty("加入会议时间")
    private LocalDateTime joinTime;


}
