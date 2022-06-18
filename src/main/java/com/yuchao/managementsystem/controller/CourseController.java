package com.yuchao.managementsystem.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuchao.managementsystem.common.Result;
import com.yuchao.managementsystem.entity.Course;
import com.yuchao.managementsystem.mapper.CourseMapper;
import com.yuchao.managementsystem.service.ICourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yuchao
 * @since 2022-05-08
 */
@RestController
@RequestMapping("/course")
@Slf4j
public class CourseController {
    @Resource
    private ICourseService courseService;
    @Resource
    private CourseMapper courseMapper;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Course course) {
        courseService.saveOrUpdate(course);
        return Result.success();
    }

    /**
     * 学生选课
     *
     * @param courseId
     * @param studentId
     * @return com.yuchao.managementsystem.common.Result
     * @author yuchao
     * @date 2022/5/11 18:23
     */
    @PostMapping("/studentCourse/{courseId}/{studentId}")
    public Result studentCourse(@PathVariable Integer courseId,
                                @PathVariable Integer studentId) {
        courseService.setStudentCourse(courseId, studentId);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id,
                         @RequestParam(defaultValue = "") String role,
                         @RequestParam(defaultValue = "0") Integer studentId) {

        if ("ROLE_STUDENT".equals(role)) {
            courseMapper.deleteStudentCourse(id, studentId);
        } else {
            courseService.removeById(id);
        }
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        courseService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(courseService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(courseService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name,
                           @RequestParam(defaultValue = "0") Integer credit,
                           @RequestParam(defaultValue = "") String attributes,
                           @RequestParam(defaultValue = "0") Integer id,
                           @RequestParam(defaultValue = "") String role) {//没有defaultValue的话前端请求不传数据会出现400

        Page<Course> page = null;

        if ("ROLE_STUDENT".equals(role)) {
            page = courseService.findStudentCourse(new Page<Course>(pageNum, pageSize), name, credit, attributes, id);
        } else {
            page = courseService.findPage(new Page<Course>(pageNum, pageSize), name, credit, attributes, id);//传进去的page是用来差total的
        }
        return Result.success(page);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Course course) {
        return Result.success(courseService.updateById(course));
    }


    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Course> list = courseService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("课程信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }

    /**
     * excel 导入
     *
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<Course> list = reader.readAll(Course.class);

        courseService.saveBatch(list);
        return Result.success(true);
    }

}


