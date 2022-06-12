package com.yuchao.managementsystem;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author 蒙宇潮
 * @create 2022-05-02  13:32
 */
@Slf4j
public class MyTest {

    @Test
    public void test(){
        log.info("这是info日志");
        //        下面的日志级别有低到高，可以调整输出的日志级别，比调整的日志级别要高的才会生效
//        logger.trace("这是trace日志");
//        logger.debug("这是debug日志");
////        springboot默认的日志级别是info ,可以在配置文件修改
//        logger.info("这是info日志");
//        logger.warn("这是warn日志");
//        logger.error("这是error日志");
    }
}
