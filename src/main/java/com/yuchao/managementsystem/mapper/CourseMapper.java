package com.yuchao.managementsystem.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuchao.managementsystem.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author yuchao
 * @since 2022-05-08
 */
public interface CourseMapper extends BaseMapper<Course> {


    Page<Course> findPage(Page<Course> coursePage, @Param("name") String name, @Param("credit") Integer credit, @Param("attributes") String attributes,@Param("id") Integer id);

    void deleteStudentCourse(@Param("courseId") Integer courseId, @Param("studentId") Integer studentId);

    void setStudentCourse(@Param("courseId") Integer courseId, @Param("studentId") Integer studentId);

    Page<Course> findStudentCourse(Page<Course> coursePage, @Param("name") String name, @Param("credit") Integer credit, @Param("attributes") String attributes, @Param("id") Integer id);

}
