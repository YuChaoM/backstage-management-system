package com.yuchao.managementsystem.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.yuchao.managementsystem.common.Constants;
import com.yuchao.managementsystem.common.Result;
import com.yuchao.managementsystem.config.AuthAccess;
import com.yuchao.managementsystem.entity.Files;
import com.yuchao.managementsystem.entity.User;
import com.yuchao.managementsystem.mapper.FileMapper;
import com.yuchao.managementsystem.service.IUserService;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 蒙宇潮
 * @create 2022-04-26  9:16
 */
@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private IUserService userService;

    @Resource
    private FileMapper fileMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;



    //假数据
    @GetMapping("/example")
    public Result get() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("x", Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        map.put("y", Arrays.asList(150, 230, 224, 218, 135, 147, 260));
        return Result.success(map);

    }

    @GetMapping("/members")
    public Result members() {
        List<User> list = userService.list();
        int q1 = 0;
        int q2 = 0;
        int q3 = 0;
        int q4 = 0;
        for (User user : list) {
            Date createTime = user.getCreateTime();
            Quarter quarter = DateUtil.quarterEnum(createTime);
            switch (quarter) {
                case Q1:
                    q1 += 1;
                    break;
                case Q2:
                    q2 += 1;
                    break;
                case Q3:
                    q3++;
                    break;
                case Q4:
                    q4++;
                    break;
            }
        }
        return Result.success(CollUtil.newArrayList(q1, q2, q3, q4));
    }

    @AuthAccess
    @GetMapping("/file/front/all")
//    @Cacheable(value = "files",key = "'frontAll'")//不要导错包
    public Result frontAll() {
        //1.从缓存获取数据
        String jsonStr = stringRedisTemplate.opsForValue().get(Constants.FILES_KEY);
        List<Files> files;
        if (StrUtil.isBlank(jsonStr)) {//如果没有
            files = fileMapper.selectList(null);//从数据库中取
            //放到redis缓存
            stringRedisTemplate.opsForValue().set(Constants.FILES_KEY, JSONUtil.toJsonStr(files));
            //缓存过期时间
            stringRedisTemplate.expire(Constants.FILES_KEY, 7200, TimeUnit.SECONDS);
        } else {
            files = JSONUtil.toBean(jsonStr, new TypeReference<List<Files>>() {
            }, true);
        }
        return Result.success(files);
    }

}
