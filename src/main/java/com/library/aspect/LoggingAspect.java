package com.library.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // Pointcut for all methods in the service package
    @Pointcut("execution(* com.library.service..*(..))")
    public void allServiceMethods() {}

    // Log entry to the method
    @Before("allServiceMethods()")
    public void logMethodEntry(JoinPoint joinPoint) {
        logger.info("Entering method: " + joinPoint.getSignature().getName());
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            logger.info("Argument: " + arg);
        }
    }

    // Log exit from the method
    @AfterReturning(pointcut = "allServiceMethods()", returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        logger.info("Exiting method: " + joinPoint.getSignature().getName() + " with result: " + result);
    }

    // Pointcut for cacheable methods annotated with @Cacheable
    @Pointcut("@annotation(cacheable)")
    public void cacheableMethods(Cacheable cacheable) {}

    // Around advice to log cache hits and misses for @Cacheable methods
    @Around("cacheableMethods(cacheable)")
    public Object logCacheableMethods(ProceedingJoinPoint joinPoint, Cacheable cacheable) throws Throwable {
        String cacheName = cacheable.value().length > 0 ? cacheable.value()[0] : "default";
        logger.info("Checking cache '{}' for method '{}'", cacheName, joinPoint.getSignature().getName());

        Object result;
        try {
            result = joinPoint.proceed();
            if (result != null) {
                logger.info("Cache miss - Result loaded from database for '{}'", joinPoint.getSignature().getName());
            } else {
                logger.info("Cache hit - Result fetched from cache for '{}'", joinPoint.getSignature().getName());
            }
        } catch (Exception e) {
            logger.error("Error during cache operation in '{}'", joinPoint.getSignature().getName(), e);
            throw e;
        }
        return result;
    }

    // Around advice to log entry, exit, and arguments of controller methods
    @Around("execution(* com.library.controller..*(..))")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("Entering {}.{} with arguments: {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), joinPoint.getArgs());
        Object result = joinPoint.proceed();
        logger.info("Exiting {}.{} with result: {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), result);
        return result;
    }
}
