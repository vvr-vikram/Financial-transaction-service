package com.example.finance.service;

import com.example.finance.entity.TransactionAudit;
import com.example.finance.repository.TransactionAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final TransactionAuditRepository auditRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAudit(String transactionId, String eventType, String message) {
        TransactionAudit audit = TransactionAudit.builder()
                .transactionId(transactionId)
                .eventType(eventType)
                .message(message)
                .build();
        auditRepository.save(audit);
    }
}
