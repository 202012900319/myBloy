package com.my.blog.controller;


import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.TagListDto;
import com.my.blog.domain.entity.Tag;
import com.my.blog.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private ITagService tagService;

    @GetMapping("/list")
    public ResponseResult getList(Integer pageNum, Integer pageSize,TagListDto tagListDto){
        return tagService.PageTageList(pageNum,pageSize,tagListDto);
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody Tag tag){
        return tagService.addTag(tag);
    }

    @DeleteMapping("{id}")
    public ResponseResult deleteTag(@PathVariable("id") Long id){
        return tagService.delectTag(id);
    }

    @GetMapping("{id}")
    public ResponseResult getId(@PathVariable("id") Long id){
        return tagService.getId(id);
    }

    @PutMapping
    public ResponseResult updataTag(@RequestBody Tag tag){
        return tagService.updataTag(tag);
    }

    @GetMapping("listAllTag")
    public ResponseResult listAllTag(){
        return tagService.listAllTag();
    }
}
