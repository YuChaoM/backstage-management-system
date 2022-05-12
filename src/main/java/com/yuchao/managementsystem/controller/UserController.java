package com.yuchao.managementsystem.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuchao.managementsystem.common.Result;
import com.yuchao.managementsystem.controller.dto.UserDTO;
import com.yuchao.managementsystem.controller.dto.UserPasswordDTO;
import com.yuchao.managementsystem.entity.User;
import com.yuchao.managementsystem.service.IUserService;
import com.yuchao.managementsystem.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

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


    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO);
    }
    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        return Result.success(userService.register(userDTO));
    }
    @PostMapping("/check")
    public Result check(@RequestBody UserDTO userDTO){
        return userService.check(userDTO);
    }
    @PostMapping("/password")
    public Result updatePassword(@RequestBody UserPasswordDTO userPasswordDTO) {
        userService.updatePassword(userPasswordDTO);
        return Result.success();
    }

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody User user) {

//        return userService.mysaveOrUpdate(user);
        userService.saveOrUpdate(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(userService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(userService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        return Result.success(userService.list());
    }

    @GetMapping("/role/{role}")
    public Result findUsersByRole(@PathVariable String role) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", role);
        List<User> list = userService.list(queryWrapper);
//        List<String> collect = list.stream().map(teacher -> teacher.getUsername()).collect(Collectors.toList());
        return Result.success(list);
    }


    @GetMapping("/username/{username}")
    public Result findOne(@PathVariable String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return Result.success(userService.getOne(queryWrapper));
    }

    //分页查询 mybatis-plus的方式，模糊查询也用它
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam(defaultValue = "") String username,
                               @RequestParam(defaultValue = "") String role,
                               @RequestParam(defaultValue = "") String address) {
//        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
//
//        if (!"".equals(username)) {//不判空的话会直接拼%%,不好
//            userQueryWrapper.like("username", username);
//        }
//        if (!"".equals(role)) {
//            userQueryWrapper.like("role", role);
//        }
//        if (!"".equals(address)) {
//            userQueryWrapper.like("address", address);
//        }
//        //获取当前用户信息
//        User currentUser = TokenUtils.getCurrentUser();
//        System.out.println(currentUser.getUsername());
        return Result.success(userService.findPage(new Page<>(pageNum, pageSize), username,role,address));
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<User> list = userService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("nickname", "昵称");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("address", "地址");
        writer.addHeaderAlias("createTime", "创建时间");
        writer.addHeaderAlias("avatarUrl", "头像");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }

    /**
     * excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<User> list = reader.readAll(User.class);

        userService.saveBatch(list);
        return Result.success(true);
    }
}

