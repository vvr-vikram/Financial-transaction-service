package com.example.finance.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "idempotency_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdempotencyRecord {
    @Id
    @Column(name = "request_key", length = 100)
    private String requestKey;

    @Column(name = "transaction_id", length = 36)
    private String transactionId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
