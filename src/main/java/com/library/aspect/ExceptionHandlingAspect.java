package com.library.aspect;

import com.library.dto.RequestStatus;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionHandlingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlingAspect.class);

    @AfterThrowing(pointcut = "execution(* com.library.service.*.*(..))", throwing = "ex")
    public void logException(Exception ex) {
        logger.error("Exception caught in service layer: ", ex);
    }

    @AfterThrowing(pointcut = "execution(* com.library.service.*.*(..))", throwing = "ex")
    public RequestStatus handleException(Exception ex) {
        if (ex instanceof IllegalArgumentException) {
            return new RequestStatus(false, "Invalid argument: " + ex.getMessage());
        } else if (ex instanceof IllegalStateException) {
            return new RequestStatus(false, "Operation not allowed: " + ex.getMessage());
        }
        return new RequestStatus(false, "An unexpected error occurred. Please try again.");
    }
}
