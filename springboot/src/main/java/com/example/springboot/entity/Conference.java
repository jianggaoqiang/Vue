package com.example.springboot.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2022-10-10
 */
@Getter
@Setter
@TableName("sys_conference")
@ApiModel(value = "Conference对象", description = "")
public class Conference implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("发起人")
    @TableField(value = "initiator")
    private String initiator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("开始时间")
    @TableField(value = "begin_time")
    private LocalDateTime beginTime;


    @ApiModelProperty("结束时间")
    @TableField(value = "end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;


    @ApiModelProperty("场所")
    @TableField(value = "room")
    private String room;

    @ApiModelProperty("简介")
    @TableField(value = "introduce")
    private String introduce;

    @ApiModelProperty("加入人数")
    @TableField(value = "join_number")
    private Integer joinNumber;

    @ApiModelProperty("签到人数")
    @TableField(value = "sign_number")

    private Integer signNumber;

    @ApiModelProperty("会议名称")
    @TableField(value = "conference_name")

    private String conferenceName;


}
