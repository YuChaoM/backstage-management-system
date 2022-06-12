package com.yuchao.managementsystem.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.lang.ObjectId;
import cn.hutool.core.util.StrUtil;
import com.yuchao.managementsystem.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
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
    public Result getKey() {
        //生成唯一的key
        String key = ObjectId.next();
        return Result.success(key);
    }

    @GetMapping
    public void getCaptcha(@RequestParam(defaultValue = "") String key, HttpServletResponse response) throws IOException {
        if (!StrUtil.isBlank(key)) {
//            GifCaptcha captcha = CaptchaUtil.createGifCaptcha(100, 36);
            ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(100, 36);
//            LineCaptcha captcha = CaptchaUtil.createLineCaptcha(100, 36);
            String code = captcha.getCode();
            ServletOutputStream os = response.getOutputStream();
            stringRedisTemplate.opsForValue().set(key, code);
            stringRedisTemplate.expire(key, 5, TimeUnit.MINUTES);
            //把图片验证码返回给前端
            captcha.write(os);
            os.close();
        }
    }
}
