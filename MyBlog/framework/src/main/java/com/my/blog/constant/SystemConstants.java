package com.my.blog.constant;

public class SystemConstants {
    /**
     * 状态是草稿状态
     */
    public static final int STATUS_DRAFT = 1;
    /**
     * 状态正常发布状态
     */
    public static final int STATUS_NORMAL = 0;

    /**
     * 分类是正常状态
     */
    public static final String CATEGORY_STATUS_NORMAL = "0";

    /**
     * 分类是禁用状态
     */
    public static final String CATEGORY_STATUS_DRAFT = "1";

    /**
     * 审核状态通过
     */
    public static final String LINK_STATUS_NORMAL = "0";

    /**
     * 审核状态未通过
     */
    public static final String LINK_STATUS_DRAFT = "1";

    /**
     * 审核状态未审核
     */
    public static final String LINK_STATUS_NO = "2";

    /**
     * 根评论的id
     */
    public static final long COMMENT_ROOT_ID = -1;

    /**
     * 评论类型为：文章评论
     */
    public static final String ARTICLE_COMMENT = "0";
    /**
     * 评论类型为：友联评论
     */
    public static final String LINK_COMMENT = "1";

}
