package com.example.springboot.entity;

import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class FaceResult {
    private Integer score;
    private String groupId;
    private String userId;
    private String userInfo;

}
