package com.my.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.constant.SystemConstants;
import com.my.blog.dao.ArticleMapper;
import com.my.blog.dao.ArticleTagMapper;
import com.my.blog.dao.CategoryMapper;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.dto.AddArticleDto;
import com.my.blog.domain.entity.Article;
import com.my.blog.domain.entity.ArticleTag;
import com.my.blog.domain.entity.Category;
import com.my.blog.domain.entity.Tag;
import com.my.blog.domain.vo.ArticleDetailVo;
import com.my.blog.domain.vo.ArticleListVo;
import com.my.blog.domain.vo.HotArticleVo;
import com.my.blog.domain.vo.PageVo;
import com.my.blog.service.IArticleService;
import com.my.blog.service.IArticleTagService;
import com.my.blog.utils.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 文章表 服务实现类
 * </p>
 *
 * @author WH
 * @since 2023-05-16
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ResponseResult hotArticleList() {

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.STATUS_NORMAL)
                .orderByDesc(Article::getViewCount);

        Page<Article> page = new Page<>(1, 10);
        articleMapper.selectPage(page,queryWrapper);
        List<Article> articles = page.getRecords();

        ArrayList<HotArticleVo> hotArticleVos = new ArrayList<>();
        for (Article article: articles){
            HotArticleVo vo = new HotArticleVo();
            BeanUtils.copyProperties(article,vo);
            hotArticleVos.add(vo);
        }

        return ResponseResult.okResult(hotArticleVos);



    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        LambdaQueryWrapper<Article> articleQuery = new LambdaQueryWrapper<>();
        articleQuery.eq(Objects.nonNull(categoryId) && categoryId > 0,Article::getCategoryId,categoryId)
                .eq(Article::getStatus,SystemConstants.STATUS_NORMAL)
                .orderByDesc(Article::getIsTop);

        Page<Article> page = new Page<>(pageNum, pageSize);
        articleMapper.selectPage(page,articleQuery);
        List<Article> articles = page.getRecords();

        ArrayList<ArticleListVo> articleListVos = new ArrayList<>();
        for (Article article : articles){
            ArticleListVo articleListVo = new ArticleListVo();
            BeanUtils.copyProperties(article,articleListVo);

            Category category = categoryMapper.selectById(article.getCategoryId());
            articleListVo.setCategoryName(category.getName());

            articleListVos.add(articleListVo);
        }

        PageVo pageVo = new PageVo(articleListVos, page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {

        Article article = articleMapper.selectById(id);
        Category category = categoryMapper.selectById(article.getCategoryId());

        ArticleDetailVo articleDetailVo = new ArticleDetailVo();
        BeanUtils.copyProperties(article,articleDetailVo);
        articleDetailVo.setCategoryName(category.getName());

        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult getAdminArticleList(Integer pageNum, Integer pageSize, String title, String summary) {
        LambdaQueryWrapper<Article> articleQueryWrapper = new LambdaQueryWrapper<>();
        articleQueryWrapper.like(title != null,Article::getTitle,title)
                .like(summary != null,Article::getSummary,summary)
                .orderByDesc(Article::getIsTop)
                .orderByDesc(Article::getCreateTime);
        Page<Article> page = new Page<>(pageNum, pageSize);
        Page<Article> articlePage = articleMapper.selectPage(page, articleQueryWrapper);
        List<Article> articles = articlePage.getRecords();
        ArrayList<ArticleListVo> articleListVos = new ArrayList<>();
        for (Article article : articles){
            ArticleListVo articleListVo = new ArticleListVo();
            BeanUtils.copyProperties(article,articleListVo);
            articleListVos.add(articleListVo);
        }
        PageVo pageVo = new PageVo(articleListVos, articlePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Autowired
    private IArticleTagService articleTagService;
    @Override
    public ResponseResult addArticle(AddArticleDto articleDto) {
        //添加博客
        Article article = new Article();
        BeanUtils.copyProperties(articleDto,article);
        save(article);
        //添加博客和tag的关联
        List<Long> tags = articleDto.getTags();
        List<ArticleTag> articleTags = tags.stream().map(tagId -> new ArticleTag(article.getId(), tagId)).collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.addViewCountCache("viewCount",id.toString(),1);
        return ResponseResult.okResult();
    }
}
