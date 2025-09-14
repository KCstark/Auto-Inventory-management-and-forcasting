package com.inventory_forcasting.repo;

import com.inventory_forcasting.enties.AuditLog;
import com.inventory_forcasting.models.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByUserIdOrderByTimestampDesc(Long userId);
    List<AuditLog> findByEventTypeOrderByTimestampDesc(EventType eventType);
}
