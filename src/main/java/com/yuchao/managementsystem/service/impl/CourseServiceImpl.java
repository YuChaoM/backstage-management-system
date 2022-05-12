package com.yuchao.managementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuchao.managementsystem.entity.Course;
import com.yuchao.managementsystem.mapper.CourseMapper;
import com.yuchao.managementsystem.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yuchao
 * @since 2022-05-08
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    @Resource
    private CourseMapper courseMapper;


    @Transactional//同时成功同时失败
    @Override
    public void setStudentCourse(Integer courseId, Integer studentId) {
        courseMapper.deleteStudentCourse(courseId, studentId);
        courseMapper.setStudentCourse(courseId, studentId);
    }

    @Override
    public Page<Course> findPage(Page<Course> coursePage, String name, Integer credit, String attributes, Integer id) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
//        if (!"".equals(name)) {
//            queryWrapper.like("name", name);
//        }
//        if (credit != 0) {
//            queryWrapper.eq("credit", credit);
//        }
//        if (!"".equals(attributes)) {
//            queryWrapper.like("attributes", attributes);
//        }
        Page<Course> page = courseMapper.findPage(coursePage, name, credit, attributes,id);
        return page;
    }

    @Override
    public Page<Course> findStudentCourse(Page<Course> coursePage, String name, Integer credit, String attributes, Integer id) {
        return courseMapper.findStudentCourse(coursePage, name, credit, attributes, id);
    }




}
