package com.yuchao.managementsystem.controller.dto;

import lombok.Data;

/**
 * @author 蒙宇潮
 * @create 2022-05-01  23:44
 */
@Data
public class UserPasswordDTO {

    private String username;
    private String password;
    private String newPassword;
    private String email;
    private String code;
}
