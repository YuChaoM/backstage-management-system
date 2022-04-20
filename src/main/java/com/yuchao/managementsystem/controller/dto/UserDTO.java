package com.yuchao.managementsystem.controller.dto;

import lombok.Data;

/**
 * @author 蒙宇潮
 * @create 2022-04-19  10:54
 * 用于前端登录的数据，实体类封装的数据太多，并不需要
 */
@Data
public class UserDTO {
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
}
