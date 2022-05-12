package com.yuchao.managementsystem.config;

import java.lang.annotation.*;

/**
 * @author 蒙宇潮
 * @create 2022-05-10  16:08
 */


@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthAccess {

}
