package com.yuchao.managementsystem.mapper;

import com.yuchao.managementsystem.controller.dto.UserPasswordDTO;
import com.yuchao.managementsystem.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yuchao
 * @since 2022-04-17
 */
public interface UserMapper extends BaseMapper<User> {

    @Update("update sys_user set password = #{newPassword} where  username = #{username} and password = #{password}")
    int updatePassword(UserPasswordDTO userPasswordDTO);
}
