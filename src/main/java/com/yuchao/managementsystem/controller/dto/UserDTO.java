package com.yuchao.managementsystem.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yuchao.managementsystem.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * @author 蒙宇潮
 * @create 2022-04-19  10:54
 * 用于前端登录的数据，实体类封装的数据太多，并不需要
 */
@Data
public class UserDTO {

    private Integer id;
    private String username;
//    @JsonIgnore,开启后前台传过来的数据没拿到
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;
    private String role;
    private List<Menu> menus;
}
