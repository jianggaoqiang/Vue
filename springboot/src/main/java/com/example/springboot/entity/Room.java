package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
@TableName("sys_room")
@ApiModel(value = "Room对象", description = "")
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("会议室名称")
    private String roomName;

    @ApiModelProperty("可容纳人数")
    private Integer roomSize;

    @ApiModelProperty("当前状态")
    private Integer roomState;

    @ApiModelProperty("门派编号")
    private Integer roomnum;


}
