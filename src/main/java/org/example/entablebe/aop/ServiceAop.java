package org.example.entablebe.aop;

import io.jsonwebtoken.ExpiredJwtException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceAop {


    @Around("execution(* org.example.entablebe.service.JwtService.*(..))")
    public Object handleExpiredJwtException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (ExpiredJwtException expiredJwtException) {
            throw new RuntimeException("Expired JWT token");
        }
    }
}
