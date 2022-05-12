package com.yuchao.managementsystem.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuchao.managementsystem.entity.Menu;
import com.yuchao.managementsystem.mapper.MenuMapper;
import com.yuchao.managementsystem.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yuchao
 * @since 2022-04-28
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Override
    public List<Menu> findMenus(String name) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort_num");
        if (StrUtil.isBlank(name)){
            queryWrapper.like("name", name);
        }
        //返回所有菜单
        List<Menu> list = list(queryWrapper);
        //根据pid为null找到一级菜单
        List<Menu> parentnNodes = list.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());
        //一级菜单的子菜单
        for (Menu menu : parentnNodes) {
            //在所有菜单中找到pid等于一级菜单的id就是子菜单
            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList()));
        }
        return parentnNodes;
    }
}
