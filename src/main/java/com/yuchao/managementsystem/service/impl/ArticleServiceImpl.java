package com.yuchao.managementsystem.service.impl;

import com.yuchao.managementsystem.entity.Article;
import com.yuchao.managementsystem.mapper.ArticleMapper;
import com.yuchao.managementsystem.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yuchao
 * @since 2022-05-22
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

}
