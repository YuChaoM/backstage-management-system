package com.yuchao.managementsystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.yuchao.managementsystem.service.IUserService;
import com.yuchao.managementsystem.entity.User;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yuchao
 * @since 2022-04-17
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService userService;

    // 新增或者更新
    @PostMapping
    public boolean save(@RequestBody User user) {
        return userService.saveOrUpdate(user);
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id) {
        return userService.removeById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return userService.removeByIds(ids);
    }

    @GetMapping
    public List<User> findAll() {
        return userService.list();
    }

    @GetMapping("/{id}")
    public User findOne(@PathVariable Integer id) {
        return userService.getById(id);
    }

    //分页查询 mybatis-plus的方式
    @GetMapping("/page")
    public Page<User> findPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam(defaultValue = "") String username,
                               @RequestParam(defaultValue = "") String email,
                               @RequestParam(defaultValue = "") String address) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        //现在模糊查询就简单了,要给参数加一个默认值，不然的话，什么都不传，就会报错
        if (!"".equals(username)) {//空的话会直接拼%%,查出的结果不对
            userQueryWrapper.like("username", username);
        }
        if (!"".equals(email)) {
            userQueryWrapper.like("email", email);
        }
        if (!"".equals(address)) {
            userQueryWrapper.like("address", address);
        }
        return userService.page(new Page<>(pageNum, pageSize), userQueryWrapper);
    }
}

