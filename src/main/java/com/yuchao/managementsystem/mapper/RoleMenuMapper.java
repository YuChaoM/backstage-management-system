package com.yuchao.managementsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuchao.managementsystem.entity.RoleMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author 蒙宇潮
 * @create 2022-04-30  23:19
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {


    @Delete("delete from sys_role_menu where role_id = #{roleId}")
    int deleteByRoleId(@Param("roleId") Integer roleId);

    @Select("select menu_id from sys_role_menu where  role_id = #{roleId}")
    List<Integer> selectByRoleId(@Param("roleId") Integer roleId);
}
