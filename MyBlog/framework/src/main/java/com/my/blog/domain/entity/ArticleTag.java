package com.my.blog.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 文章标签关联表
 * </p>
 *
 * @author WH
 * @since 2023-06-13
 */
@TableName("article_tag")
@AllArgsConstructor
@NoArgsConstructor
public class ArticleTag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章id
     */
    @TableId(value = "article_id", type = IdType.AUTO)
    private Long articleId;

    /**
     * 标签id
     */
    private Long tagId;

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    @Override
    public String toString() {
        return "ArticleTag{" +
            "articleId = " + articleId +
            ", tagId = " + tagId +
        "}";
    }
}
