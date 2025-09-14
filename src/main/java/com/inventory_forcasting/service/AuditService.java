package com.inventory_forcasting.service;

import com.inventory_forcasting.enties.AuditLog;
import com.inventory_forcasting.models.EventType;
import com.inventory_forcasting.repo.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {
    private final AuditLogRepository auditLogRepository;
    private final GetEverywhere getEverywhere;

    @Async
    public void logEvent(EventType eventType, String endpoint, String details) {
        Long userId;
        if(eventType==EventType.USER_LOGOUT || eventType==EventType.USER_LOGIN
                ||endpoint.equalsIgnoreCase("ASPECT: UserController.loginUser")
                ||endpoint.equalsIgnoreCase("ASPECT: UserController.logoutUser"))
            userId=null;
        else
            userId=getEverywhere.userId();
        try {
            AuditLog auditLog = AuditLog.builder()
                    .eventType(eventType)
                    .endpoint(endpoint)
                    .details(details)
                    .userId(userId)
                    .build();

            if(!endpoint.startsWith("ASPECT: "))//not saving the audit log for aspect
                auditLogRepository.save(auditLog);
            log.info("Audit logged: {} by user: {}", eventType, userId);

        } catch (Exception e) {
            log.error("Failed to save audit log for event: {}", eventType, e);
        }
    }

}
