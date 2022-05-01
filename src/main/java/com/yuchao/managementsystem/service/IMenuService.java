package com.yuchao.managementsystem.service;

import com.yuchao.managementsystem.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yuchao
 * @since 2022-04-28
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> findMenus(String name);
}
