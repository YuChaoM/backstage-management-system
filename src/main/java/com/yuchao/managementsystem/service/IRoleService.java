package com.yuchao.managementsystem.service;

import com.yuchao.managementsystem.entity.Role;
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
public interface IRoleService extends IService<Role> {

    void setRoleMenu(Integer roleId, List<Integer> menuId);

    List<Integer> getRoleMenu(Integer roleId);
}
