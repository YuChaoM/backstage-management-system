package com.yuchao.managementsystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuchao.managementsystem.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yuchao
 * @since 2022-05-08
 */
public interface ICourseService extends IService<Course> {

    void setStudentCourse(Integer courseId, Integer studentId);

    Page<Course> findPage(Page<Course> coursePage, String name, Integer credit, String attributes, Integer id);

    Page<Course> findStudentCourse(Page<Course> coursePage, String name, Integer credit, String attributes, Integer id);


}
