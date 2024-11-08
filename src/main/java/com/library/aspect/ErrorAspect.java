package com.library.aspect;

import com.library.dto.RequestStatus;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ErrorAspect {

    private static final Logger logger = LoggerFactory.getLogger(ErrorAspect.class);

    // Pointcut for all methods in the service package
    @Pointcut("execution(* com.library.service..*(..))")
    public void allServiceMethods() {}

    // Log error messages when exceptions occur in service methods
    @AfterThrowing(pointcut = "allServiceMethods()", throwing = "error")
    public void logServiceError(JoinPoint joinPoint, Throwable error) {
        logger.error("Error in service method: {} - {}", joinPoint.getSignature().getName(), error.getMessage(), error);
    }

    // Pointcut to match all methods in controller classes
    @Pointcut("within(com.library.controller..*)")
    public void controllerMethods() {}

    // Handle and log exceptions in controller methods and return a standard error response
    @AfterThrowing(pointcut = "controllerMethods()", throwing = "ex")
    public ResponseEntity<RequestStatus> handleControllerException(JoinPoint joinPoint, Exception ex) {
        logger.error("Exception in controller method: {} - {}", joinPoint.getSignature().getName(), ex.getMessage(), ex);

        RequestStatus errorResponse;
        if (ex instanceof IllegalArgumentException) {
            errorResponse = new RequestStatus(false, "Invalid input: " + ex.getMessage());
        } else if (ex instanceof IllegalStateException) {
            errorResponse = new RequestStatus(false, "Operation not allowed: " + ex.getMessage());
        } else {
            errorResponse = new RequestStatus(false, "An unexpected error occurred. Please try again.");
        }

        return ResponseEntity.badRequest().body(errorResponse);
    }
}
