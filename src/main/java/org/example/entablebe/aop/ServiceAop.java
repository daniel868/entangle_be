package org.example.entablebe.aop;

import io.jsonwebtoken.ExpiredJwtException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceAop {

    private static final Logger logger = LogManager.getLogger(ServiceAop.class);

    @Around("execution(* org.example.entablebe.service.JwtService.*(..))")
    public Object handleExpiredJwtException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (ExpiredJwtException expiredJwtException) {
            throw new RuntimeException("Expired JWT token");
        }
    }

    @Around("execution(* org.example.entablebe.service..*(..)) && !execution(* org.example.entablebe.service.UserInfoService.loadProfileImageAsBase64())")
    public Object logMethodAndArguments(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        logger.debug("Start method {}, arguments: {}", methodName, arguments);
        Object result = joinPoint.proceed();
        logger.debug("Finishing executing method {}, result: {}", methodName, result);
        return result;
    }
}
