package com.yuchao.managementsystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author 蒙宇潮
 * @create 2022-04-30  20:25
 */

@Data
@TableName("sys_dict")
public class Dict {

    private String name;
    private String value;
    private String type;
}
