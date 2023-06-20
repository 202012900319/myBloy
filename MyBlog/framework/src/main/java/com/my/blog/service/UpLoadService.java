package com.my.blog.service;

import com.my.blog.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface UpLoadService {
    ResponseResult upLoad(MultipartFile img);

}
