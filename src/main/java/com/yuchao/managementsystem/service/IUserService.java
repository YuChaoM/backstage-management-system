package com.yuchao.managementsystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuchao.managementsystem.common.Result;
import com.yuchao.managementsystem.controller.dto.UserDTO;
import com.yuchao.managementsystem.controller.dto.UserPasswordDTO;
import com.yuchao.managementsystem.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.mail.MessagingException;

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

    Result mysaveOrUpdate(User user);

    Page<User>  findPage(Page<User> objectPage, String username, String role, String address);

    Result loginByEmail(UserDTO userDTO, String key);

    void sendEmailCode(String email, Integer key, String s) throws MessagingException;

    void restPassword(UserPasswordDTO userPasswordDTO, String key);

    Result getAvatarUrl(Integer type, String temp);
}
