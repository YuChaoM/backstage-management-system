package com.yuchao.managementsystem.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.GifCaptcha;
import cn.hutool.core.lang.ObjectId;
import cn.hutool.core.util.StrUtil;
import com.yuchao.managementsystem.common.Constants;
import com.yuchao.managementsystem.common.Result;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author 蒙宇潮
 * @create 2022-05-14  14:16
 */

@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/getkey")
    public Result getKey(){
        //生成唯一的key
        String key = ObjectId.next();
        return Result.success(key);
    }

    @GetMapping
    public void getCaptcha(@RequestParam(defaultValue = "") String key, HttpServletResponse response) throws IOException {
        if (!StrUtil.isBlank(key)) {
            GifCaptcha gifCaptcha = CaptchaUtil.createGifCaptcha(100, 36);
            String code = gifCaptcha.getCode();
            ServletOutputStream os = response.getOutputStream();
            stringRedisTemplate.opsForValue().set(key, code);
            stringRedisTemplate.expire(key, 5, TimeUnit.MINUTES);
            //把图片验证码返回给前端
            gifCaptcha.write(os);
            os.close();
        }
//        return Result.success(code);
    }
}
