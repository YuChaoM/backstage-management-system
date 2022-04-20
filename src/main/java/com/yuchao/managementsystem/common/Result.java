package com.yuchao.managementsystem.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 蒙宇潮
 * @create 2022-04-19  16:38
 * 统一接口的返回值,不然前端的返回值五花八门
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private String code;
    private String msg;
    private Object data;

    public static Result success(){
        return new Result(Constants.CODE_200,"",null);
    }


    public static Result success(Object data){
        return new Result(Constants.CODE_200,"",data);
    }


    public static Result error(String code,String msg){
        return new Result(code,msg,null);
    }


    public static Result error(){
        return new Result(Constants.CODE_500,"系统错误",null);
    }
}
