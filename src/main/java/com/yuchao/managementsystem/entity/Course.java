package com.yuchao.managementsystem.entity;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author yuchao
 * @since 2022-05-08
 */
@Data
@ApiModel(value = "Course对象", description = "")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("课程编号")
    private String courseNum;

    @ApiModelProperty("课程名称")
    private String name;

    @ApiModelProperty("学分")
    private Integer credit;

    @ApiModelProperty("课程性质")
    private String nature;

    @ApiModelProperty("课程属性")
    private String attributes;

    private Integer teacherId;

    @ApiModelProperty("学时")
    private Integer times;


    @ApiModelProperty("是否开课")
    private Boolean status;

    @TableField(exist = false)//标识这个字段在数据库表不存在，不然mybatis plus默认会加载所有字段，匹配不到就会报错
    private String teacher;

}
