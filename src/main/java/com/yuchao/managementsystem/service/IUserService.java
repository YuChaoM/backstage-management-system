package com.yuchao.managementsystem.service;

import com.yuchao.managementsystem.common.Result;
import com.yuchao.managementsystem.controller.dto.UserDTO;
import com.yuchao.managementsystem.controller.dto.UserPasswordDTO;
import com.yuchao.managementsystem.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yuchao
 * @since 2022-04-17
 */
public interface IUserService extends IService<User> {

    Result login(UserDTO user);

    Result register(UserDTO userdto);

    Result check(UserDTO userDTO);

    void updatePassword(UserPasswordDTO userPasswordDTO);
}
