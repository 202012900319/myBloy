package com.my.blog.controller;

import com.my.blog.domain.ResponseResult;
import com.my.blog.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class AdminArtcleController {

    @Autowired
    private IArticleService articleService;

    @GetMapping("/list")
    @PreAuthorize("@permissionService.hasPermission('content:article:list')")
    public ResponseResult list(Integer pageNum, Integer pageSize, String title, String summary){
        ResponseResult articles = articleService.getAdminArticleList(pageNum,pageSize,title,summary);
        return articles;
    }

}
