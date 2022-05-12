package com.yuchao.managementsystem.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.org.apache.regexp.internal.RE;
import com.yuchao.managementsystem.common.Constants;
import com.yuchao.managementsystem.common.Result;
import com.yuchao.managementsystem.common.RoleEnum;
import com.yuchao.managementsystem.controller.dto.UserDTO;
import com.yuchao.managementsystem.controller.dto.UserPasswordDTO;
import com.yuchao.managementsystem.entity.Menu;
import com.yuchao.managementsystem.entity.User;
import com.yuchao.managementsystem.exception.ServiceException;
import com.yuchao.managementsystem.mapper.RoleMapper;
import com.yuchao.managementsystem.mapper.RoleMenuMapper;
import com.yuchao.managementsystem.mapper.UserMapper;
import com.yuchao.managementsystem.service.IMenuService;
import com.yuchao.managementsystem.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.yuchao.managementsystem.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private IMenuService menuService;
    @Resource
    private UserMapper userMapper;

    @Override
    public Result login(UserDTO userdto) {
        User one = (User) getUserInfo(userdto, "login").getData();
        if (one != null) {
            BeanUtil.copyProperties(one, userdto, true);
            //设置token
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            userdto.setToken(token);

            String role = one.getRole();
            //获取用户的菜单列表
            List<Menu> roleMenus = getRoleMenu(role);
            userdto.setMenus(roleMenus);
            return Result.success(userdto);
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    @Override
    public Result register(UserDTO userdto) {
        User user = (User) getUserInfo(userdto, "register").getData();//先检验数据库没有再存
        if (user == null) {
            user = new User();
            BeanUtil.copyProperties(userdto, user, true);
            user.setRole(RoleEnum.ROLE_USER.toString());//默认设置为普通用户
            save(user);//存进数据库里面
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名已存在");
        }
        return Result.success(user);
    }

    @Override
    public Result check(UserDTO userDTO) {
        User check = (User) getUserInfo(userDTO, "check").getData();
        if (check != null) {
//            throw new ServiceException(Constants.CODE_600, "用户名已存在");
//            return Result.error(Constants.CODE_600, "用户名已存在");
            return Result.error(Constants.CODE_600, "有内鬼，交易终止");
        }
        return Result.success();
    }

    @Override
    public void updatePassword(UserPasswordDTO userPasswordDTO) {
        int update = userMapper.updatePassword(userPasswordDTO);
        if (update < 1) {
            throw new ServiceException(Constants.CODE_600, "密码错误");
        }

    }

    @Override
    public Result mysaveOrUpdate(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtil.copyProperties(user,userDTO,true);
        Result check = check(userDTO);
        if(check.getCode() == Constants.CODE_200){
            saveOrUpdate(user);
            return Result.success();
        }else return check;
    }

    @Override
    public Page<User> findPage(Page<User> page, String username, String role, String address) {
        return userMapper.fingPage(page, username, role, address);
    }

    private Result getUserInfo(UserDTO user, String status) {
        String username = user.getUsername();
        String password = user.getPassword();
        if ("check".equals(status) && !StrUtil.isBlank(username)) {
            //不做任何行为，就是为了跳过下面的判断
        } else if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {//注册和登录会传密码
            return Result.error(Constants.CODE_400, "参数错误");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();//去数据库查必须是实体类
        queryWrapper.eq("username", username);// 等价 SQL 语句：name = '张三' nq不等于
        if ("login".equals(status)) {
            queryWrapper.eq("password", password);
        }
        User one = null;
        try {
            //查询一条记录，有脏数据有多条，会报系统错误
            one = getOne(queryWrapper);
        } catch (Exception e) {
            log.info("{}", e);
            throw new ServiceException(Constants.CODE_500, "系统错误");//会被捕获
        }
        return Result.success(one);
    }
    /**
     * 获取当前角色的菜单列表
     * @author yuchao
     * @date 2022/5/1 13:58
     * @param role
     * @return java.util.List<com.yuchao.managementsystem.entity.Menu>
     */
    private List<Menu> getRoleMenu(String role){
        Integer roleId = roleMapper.selectByFlag(role);
        //当前角色可以拥有的菜单
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);
        //查出系统的所有菜单
        List<Menu> menus = menuService.findMenus("");
        ArrayList<Menu> roleMenus = new ArrayList<>();

        for (Menu menu : menus) {
            //筛选当前用户的菜单
            if (menuIds.contains(menu.getId())){
                roleMenus.add(menu);
            }
            //当前菜单的子菜单，数据库没有，是在MenuService利用id和pid得到的
            List<Menu> children = menu.getChildren();
            //在当前菜单的子菜单中移除当前角色不可以拥有的子菜单
            children.removeIf(child -> !menuIds.contains(child.getId()));

        }
        return roleMenus;
    }

}
