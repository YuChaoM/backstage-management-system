package com.yuchao.managementsystem.config;

import com.yuchao.managementsystem.config.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 蒙宇潮
 * @create 2022-04-21  20:34
 * 1.编写一个拦截器实现HandlerInterceptor接口
 * 2.拦截器注入到容器中，（通过实现WebMvcConfigurer的addInterceptors）
 * 3.指定拦截规则
 * @EnableWebMvc:全面接管 1、静态资源？视图解析器？欢迎页.....全部失效
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(LoginInterceptor())//通过LoginInterceptor写的策略，决定是否登录，没登录就拦截
                .addPathPatterns("/**")//拦截所有,静态资源也被拦截了，css样式等
                //额外放行的，另外一种方法是配置静态资源的访问前缀,这里开放了，不登录的导入和导出功能 "/file/**",
//                .excludePathPatterns("/user/login", "/user/register","/user/check" ,"/**/export", "/**/import","/file/**",
                .excludePathPatterns("/user/login", "/user/register", "/user/check","/file/upload","/course/export","/course/import","/captcha/**",
                        "/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/api", "/api-docs", "/api-docs/**")
                .excludePathPatterns("/**/*.html", "/**/*.js", "/**/*.css", "/**/*.woff", "/**/*.ttf", "/**/*.png", "/**/*.jpg");

    }

    @Bean
    public LoginInterceptor LoginInterceptor() {
        return new LoginInterceptor();
    }
}
