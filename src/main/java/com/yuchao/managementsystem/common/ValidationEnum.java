package com.yuchao.managementsystem.common;

/**
 * @author 蒙宇潮
 * @create 2022-05-15  22:47
 */
public enum ValidationEnum {

    LOGIN(1),
    FORGET_PASS(2);
    private Integer type;

    ValidationEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
