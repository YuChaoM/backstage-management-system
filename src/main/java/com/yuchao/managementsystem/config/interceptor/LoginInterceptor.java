package com.yuchao.managementsystem.config.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.yuchao.managementsystem.common.Constants;
import com.yuchao.managementsystem.config.AuthAccess;
import com.yuchao.managementsystem.entity.User;
import com.yuchao.managementsystem.exception.ServiceException;
import com.yuchao.managementsystem.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author 蒙宇潮
 * @create 2022-04-21  20:19
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 目标方法执行之前
     */

    @Autowired
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println(request.getSession().getAttribute("captchaCode"));
        String token = request.getHeader("token");
        log.info(token);
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;//放行
        } else {
            HandlerMethod h = (HandlerMethod) handler;
            AuthAccess authAccess = h.getMethodAnnotation(AuthAccess.class);
            if (authAccess != null) {
                return true;
            }
        }
        // 执行认证
        if (StrUtil.isBlank(token) || "undefined".equals(token)) {
            //所有认证不通过，可以把页面转发到登录页
            throw new ServiceException(Constants.CODE_401, "无token，请重新登录");
        }
        // 获取 token 中的 user id
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);//从载荷中获取id
        } catch (JWTDecodeException j) {
            throw new ServiceException(Constants.CODE_401, "token验证失败，请重新登录");
        }
        // 根据token中的userid查询数据库
        User user = userService.getById(userId);
        if (user == null) {//说明token不合法
            throw new ServiceException(Constants.CODE_401, "用户不存在，请重新登录");
        }
        // 用户密码加签验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token); // 验证token
        } catch (JWTVerificationException e) {
            throw new ServiceException(Constants.CODE_401, "token验证失败，请重新登录");
        }
        return true;
    }
//    @Override 用的是session
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        StringBuffer url = request.getRequestURL();
//        log.info("拦截的请求是{}", url);
//        //登录检查逻辑
//        HttpSession session = request.getSession();
//        Object loginUser = session.getAttribute("loginUser");
//        if (loginUser != null) {
//            return true;//放行
//        }
//        //拦截住，就是未登录，跳转到登录页
////        session.setAttribute("msg", "请登录");
//        request.setAttribute("msg", "请登录");
////        response.sendRedirect("/");//重定向，前端取不到msg
//        //用转发
//        request.getRequestDispatcher("/").forward(request,response);
//        return false;
//    }
//

}
