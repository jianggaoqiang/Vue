package com.example.springboot.service;


import com.example.springboot.entity.Image;
import com.example.springboot.entity.ImageResult;
import org.json.JSONException;
import org.springframework.stereotype.Service;

@Service
public interface FaceRegisterService {
    ImageResult register(Image image) throws JSONException;

    boolean search(Image image)  throws JSONException;
}
