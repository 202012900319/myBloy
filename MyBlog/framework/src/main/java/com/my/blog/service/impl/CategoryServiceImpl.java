package com.my.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.constant.SystemConstants;
import com.my.blog.dao.ArticleMapper;
import com.my.blog.dao.CategoryMapper;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Article;
import com.my.blog.domain.entity.Category;
import com.my.blog.domain.vo.CategoryVo;
import com.my.blog.service.ICategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 分类表 服务实现类
 * </p>
 *
 * @author WH
 * @since 2023-05-16
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public ResponseResult getCategoryList() {
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.STATUS_NORMAL);

        List<Article> articles = articleMapper.selectList(articleWrapper);

        //去重
        Set<Long> categoryIds = articles.stream().map(article -> article.getCategoryId()).collect(Collectors.toSet());

        //获取去重后分类
        List<Category> categories = categoryMapper.selectBatchIds(categoryIds);

        //过滤掉禁用状态的分类
        List<Category> collect = categories.stream().filter(category -> category.getStatus().equals(SystemConstants.CATEGORY_STATUS_NORMAL)).collect(Collectors.toList());

        ArrayList<CategoryVo> categoryVos = new ArrayList<>();
        for (Category category : collect){
            CategoryVo categoryVo = new CategoryVo();
            BeanUtils.copyProperties(category,categoryVo);
            categoryVos.add(categoryVo);
        }


        return ResponseResult.okResult(categoryVos);

    }

    @Override
    public ResponseResult listAllCategory() {
        LambdaQueryWrapper<Category> categoryQueryWrapper = new LambdaQueryWrapper<>();
        categoryQueryWrapper.eq(Category::getStatus,SystemConstants.STATUS_NORMAL);
        List<Category> categories = categoryMapper.selectList(categoryQueryWrapper);
        //去重
        List<Long> categoriesCollect = categories.stream().map(category -> category.getId()).distinct().collect(Collectors.toList());
        categories = categoryMapper.selectBatchIds(categoriesCollect);
        ArrayList<CategoryVo> categoryVos = new ArrayList<>();
        for (Category category : categories){
            CategoryVo categoryVo = new CategoryVo();
            BeanUtils.copyProperties(category,categoryVo);
            categoryVos.add(categoryVo);
        }
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult getList(Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(Category::getStatus, SystemConstants.STATUS_NORMAL);
        Page<Category> categoryPage = new Page<>(pageNum, pageSize);
        Page<Category> categorys = categoryMapper.selectPage(categoryPage, null);

        return ResponseResult.okResult(categorys);
    }
}
