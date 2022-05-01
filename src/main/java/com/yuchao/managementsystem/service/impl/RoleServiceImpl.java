package com.yuchao.managementsystem.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuchao.managementsystem.entity.Menu;
import com.yuchao.managementsystem.entity.Role;
import com.yuchao.managementsystem.entity.RoleMenu;
import com.yuchao.managementsystem.mapper.RoleMapper;
import com.yuchao.managementsystem.mapper.RoleMenuMapper;
import com.yuchao.managementsystem.service.IMenuService;
import com.yuchao.managementsystem.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yuchao
 * @since 2022-04-28
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private IMenuService menuService;


    @Transactional//回滚，保证删除和添加同时成功
    @Override
    public void setRoleMenu(Integer roleId, List<Integer> menuIds) {
//        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("role_id", roleId);
//        roleMenuMapper.delete(queryWrapper);

        //先删除当前角色和菜单的所有绑定关系
        roleMenuMapper.deleteByRoleId(roleId);
        //再把前端传过来的菜单id数组绑定到当前的这个角色的id上去，但是二级菜单不会把他的父级id传过来
        ArrayList<Integer> copyMenyIds = CollUtil.newArrayList(menuIds);
        for (Integer menuId : menuIds) {
            Menu menu = menuService.getById(menuId);
            Integer pid = menu.getPid();
            if ( pid != null && !copyMenyIds.contains(pid)){
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(pid);
                roleMenuMapper.insert(roleMenu);
                copyMenyIds.add(pid);
            }
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insert(roleMenu);
        }
    }

    @Override
    public List<Integer> getRoleMenu(Integer roleId) {
        return roleMenuMapper.selectByRoleId(roleId);
    }
}
