package com.my.blog.controller;

import com.my.blog.domain.ResponseResult;
import com.my.blog.service.UpLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UpLoadController {

    @Autowired
    private UpLoadService upLoadService;

    @PostMapping("/upload")
    public ResponseResult upLoad(MultipartFile img){
        return upLoadService.upLoad(img);
    }


}
