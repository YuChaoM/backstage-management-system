package com.yuchao.managementsystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author 蒙宇潮
 * @create 2022-04-30  23:17
 */
@TableName("sys_role_menu")
@Data
public class RoleMenu {

    private Integer roleId;
    private Integer menuId;

}
