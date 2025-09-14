package com.inventory_forcasting.enties;

import com.inventory_forcasting.models.EventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime timestamp;
    private Long userId;
    private EventType eventType;
    private String endpoint;
    private String details;
    private String correlationId;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
        if (correlationId == null) {
            correlationId = UUID.randomUUID().toString().substring(0, 8);
        }
    }
}
