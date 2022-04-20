package com.yuchao.managementsystem.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuchao.managementsystem.common.Constants;
import com.yuchao.managementsystem.common.Result;
import com.yuchao.managementsystem.controller.dto.UserDTO;
import com.yuchao.managementsystem.entity.User;
import com.yuchao.managementsystem.exception.ServiceException;
import com.yuchao.managementsystem.mapper.UserMapper;
import com.yuchao.managementsystem.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yuchao
 * @since 2022-04-17
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public Result login(UserDTO userdto) {
        User one = (User) getUserInfo(userdto,"login").getData();
        if (one != null) {
            BeanUtil.copyProperties(one, userdto, true);
            return Result.success(userdto);
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    @Override
    public Result register(UserDTO userdto) {
        User user = (User) getUserInfo(userdto,"register").getData();//先检验数据库没有再存
        if (user == null){
             user = new User();
            BeanUtil.copyProperties(userdto, user, true);
            save(user);//存进数据库里面
        }else {
            throw new ServiceException(Constants.CODE_600, "用户名已存在");
        }
        return Result.success(user);
    }

    @Override
    public Result check(UserDTO userDTO) {
        User check = (User) getUserInfo(userDTO, "check").getData();
        if (check != null){
//            throw new ServiceException(Constants.CODE_600, "用户名已存在");
            return Result.error(Constants.CODE_600, "用户名已存在");
        }
        return Result.success();
    }

    private Result getUserInfo(UserDTO user,String status) {
        String username = user.getUsername();
        String password = user.getPassword();
        if ("check".equals(status)&&!StrUtil.isBlank(username)){

        } else if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();//去数据库查必须是实体类
        queryWrapper.eq("username", username);// 等价 SQL 语句：name = '张三' nq不等于
        if ("login".equals(status)){
            queryWrapper.eq("password", password);
        }
        User one = null;
        try {
            //查询一条记录，有脏数据有多条，会报系统错误
            one = getOne(queryWrapper);
        } catch (Exception e) {
            log.info("{}", e);
            throw new ServiceException(Constants.CODE_500, "系统错误");//会被捕获吗
        }
        return Result.success(one);
    }

}
