package com.bagautdinov.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    public static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.bagautdinov.service..*.*(..)) && !within(com.bagautdinov.dto..*) && !within(com.bagautdinov.config..*)")
    public void logExecution() {

    }

    @Pointcut("@annotation(com.bagautdinov.aop.Loggable)")
    public void logAnnotated() {

    }

    @Around("logExecution() || logAnnotated()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        LOGGER.info("Executing method {} in class {}", methodName, className);
        try {
            Object result = joinPoint.proceed();
            LOGGER.info("Method {} in class {} executed successfully", methodName, className);
            return result;
        } catch (Throwable throwable) {
            LOGGER.error("Method {} in class {} failed", methodName, className, throwable);
            throw throwable;
        }
    }
}
