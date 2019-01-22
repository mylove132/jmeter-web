package com.okjiaoyu.jmeter.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.mp4parser.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @Author: liuzhanhui
 * @Decription:
 * @Date: Created in 2019-01-22:13:22
 * Modify date: 2019-01-22:13:22
 */
@Component  //声明组件
@Aspect //  声明切面
@ComponentScan  //组件自动扫描
@EnableAspectJAutoProxy //spring自动切换JDK动态代理和CGLIB
public class LogAspect {
    @Resource
    HttpServletRequest request;
    /**
     *自定义日志
     */
    private Logger logger = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 在方法执行前进行切面
     */
    @Pointcut("execution(* com.okjiaoyu.jmeter..*.*(..)) && (@annotation(org.springframework.web.bind.annotation.GetMapping)||@annotation(org.springframework.web.bind.annotation.PutMapping)||@annotation(org.springframework.web.bind.annotation.DeleteMapping)||@annotation(org.springframework.web.bind.annotation.PostMapping)||@annotation(org.springframework.web.bind.annotation.RequestMapping))")
    public void log() {
    }

    @Before("log()")
    public void before(JoinPoint point) {
        logger.info("请求类方法:"+point.getSignature());
        logger.info("请求类方法参数:"+ Arrays.toString(point.getArgs()));
    }
}

