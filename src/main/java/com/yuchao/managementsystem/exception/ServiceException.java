package com.yuchao.managementsystem.exception;

/**
 * @author 蒙宇潮
 * @create 2022-04-19  17:01
 */

import lombok.Getter;

/**
 * 自定义异常
 */
@Getter
public class ServiceException extends RuntimeException {
    private String code;

    public ServiceException(String code, String msg) {
        super(msg);
        this.code = code;
    }

}
