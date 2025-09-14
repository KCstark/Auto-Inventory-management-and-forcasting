package com.inventory_forcasting.techs;

import com.inventory_forcasting.models.EventType;
import com.inventory_forcasting.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    private final AuditService auditService;

    public LoggingAspect(AuditService auditService) {
        this.auditService = auditService;
    }

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object logApiCalls(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String endpoint = "ASPECT: "+className + "." + methodName;

        log.info("API Call Started: {}", endpoint);

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;

            log.info("API Call Completed: {} in {}ms", endpoint, duration);
            auditService.logEvent(EventType.API_REQUEST, endpoint,
                    "Success - Duration: " + duration + "ms");

            return result;

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;

            log.error("API Call Failed: {} in {}ms - Error: {}", endpoint, duration, e.getMessage());
            auditService.logEvent(EventType.API_REQUEST, endpoint,
                    "Failed - Duration: " + duration + "ms - Error: " + e.getMessage());

            throw e;
        }
    }
}
