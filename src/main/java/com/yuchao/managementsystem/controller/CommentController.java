package com.yuchao.managementsystem.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuchao.managementsystem.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuchao.managementsystem.common.Result;

import com.yuchao.managementsystem.service.ICommentService;
import com.yuchao.managementsystem.entity.Comment;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *评论
 * @author yuchao
 * @since 2022-05-22
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private ICommentService commentService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Comment comment) {
        if (comment.getId() == null) {//新增的评论
            comment.setUserId(TokenUtils.getCurrentUser().getId());
            comment.setTime(DateUtil.now());
            Integer pid = comment.getPid();
            if (pid != null){//如果是回复前端就会把pid传回来
                Comment pcomm = commentService.getById(pid);
                Integer originId = pcomm.getOriginId();
                if (originId != null) {
                    comment.setOriginId(originId);//如果有设置同源
                }else {
                    comment.setOriginId(pid);//没有就把他的父级设置为祖宗
                }
            }
        }

        commentService.saveOrUpdate(comment);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        commentService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        commentService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(commentService.list());
    }

    @GetMapping("/tree/{articleId}")
    public Result findTree(@PathVariable Integer articleId) {
        List<Comment> comments = commentService.findCommentDetail(articleId);//所有评论数据
        //最顶级评论
        List<Comment> orignList = comments.stream().filter(comment -> comment.getOriginId() == null).collect(Collectors.toList());

        for (Comment orign : orignList) {
            //最顶级评论的下级评论,可能是下多级
            List<Comment> list = comments.stream().filter(comment -> orign.getId().equals(comment.getOriginId())).collect(Collectors.toList());
            list.forEach(comment -> {
                //找到当前评论的父级
                comments.stream().filter(c1 -> c1.getId().equals(comment.getPid())).findFirst().ifPresent(v -> {
                    comment.setPUserId(v.getUserId());
                    comment.setPNickname(v.getNickname());//回复的是谁
                });
            });

            orign.setChildren(list);

        }
        return Result.success(orignList);
    }


    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(commentService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(commentService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}


