package com.my.blog.service;

import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.TagListDto;
import com.my.blog.domain.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 标签 服务类
 * </p>
 *
 * @author WH
 * @since 2023-06-13
 */
public interface ITagService extends IService<Tag> {

    ResponseResult PageTageList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(Tag tag);

    ResponseResult delectTag(Long id);

    ResponseResult updataTag(Tag tag);

    ResponseResult getId(Long id);

    ResponseResult listAllTag();
}
