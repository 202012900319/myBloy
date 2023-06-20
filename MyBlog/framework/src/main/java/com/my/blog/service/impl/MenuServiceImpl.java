package com.my.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.constant.SystemConstants;
import com.my.blog.dao.MenuMapper;
import com.my.blog.domain.entity.Menu;
import com.my.blog.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author WH
 * @since 2023-06-11
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<String> selectPermsByUserId(Long id) {
        if (id == 1L){
            LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
            menuLambdaQueryWrapper.in(Menu::getMenuName,"C","F")
                    .eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> menus = menuMapper.selectList(menuLambdaQueryWrapper);
            List<String> perms = menus.stream().map(Menu::getPerms).collect(Collectors.toList());
            return perms;
        }
        return menuMapper.selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        List<Menu> menus = null;
        if (userId == 1L){
            menus = menuMapper.selectAllRouterMenu();
        }else {
            menus = menuMapper.selectRouterMenuByUserId(userId);
        }

        //构建menuTree
        List<Menu> menuTrees = buildMenuTree(menus);

        return menuTrees;
    }

    private List<Menu> buildMenuTree(List<Menu> menus) {
        List<Menu> menuTrees = menus.stream()
                //获取一级菜单
                .filter(menu -> menu.getParentId().equals(0L))
                //查询并设置子菜单
                .map(menu -> menu.setChildren(getChildren(menus, menu.getId())))
                .collect(Collectors.toList());
        return menuTrees;
    }

    private List<Menu> getChildren(List<Menu> menus, Long id) {
        List<Menu> childrenList = menus.stream()
                .filter(menu -> menu.getParentId().equals(id))
                .collect(Collectors.toList());
        return childrenList;
    }
}
