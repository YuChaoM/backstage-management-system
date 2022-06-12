package com.yuchao.managementsystem.mapper;

import com.yuchao.managementsystem.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yuchao
 * @since 2022-05-22
 */
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("select c.*, u.nickname,u.avatar_url from t_comment c left join sys_user u on c.user_id = u.id " +
            "where c.article_id = #{articleId} order by time desc ")
    List<Comment> findCommentDetail(@Param("articleId") Integer articleId);
}
