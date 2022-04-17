package com.yuchao.managementsystem.service.impl;

import com.yuchao.managementsystem.entity.User;
import com.yuchao.managementsystem.mapper.UserMapper;
import com.yuchao.managementsystem.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yuchao
 * @since 2022-04-17
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
