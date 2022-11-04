package com.example.springboot.service;


import com.example.springboot.entity.Image;
import com.example.springboot.entity.ImageResult;
import org.json.JSONException;
import org.springframework.stereotype.Service;

@Service
public interface FaceSearchService {
    ImageResult faceLogin(Image image) throws JSONException;
}
