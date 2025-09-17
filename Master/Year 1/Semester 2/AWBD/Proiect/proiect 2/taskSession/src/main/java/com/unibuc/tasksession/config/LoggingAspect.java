package com.unibuc.tasksession.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    @Pointcut("@annotation(com.unibuc.tasksession.config.Log)")
    public void logAnnotation() {}

    @Pointcut("within(com.unibuc.tasksession..*)")
    public void logPackage() {}

    @After("logAnnotation()")
    public void afterLog(JoinPoint jp) {
        log.info("[@Log] after: " + jp.getSignature());
    }

    @Before("logPackage()")
    public void beforeLog(JoinPoint jp) {
        log.info("[pkg] before: " + jp.getSignature());
    }
}
