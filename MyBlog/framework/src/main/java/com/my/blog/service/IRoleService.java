package com.my.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.domain.entity.Role;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author WH
 * @since 2023-06-11
 */
public interface IRoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);
}
