package com.example.finance.service;

import com.example.finance.entity.IdempotencyRecord;
import com.example.finance.exception.IdempotentRequestException;
import com.example.finance.repository.IdempotencyRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

    private final IdempotencyRecordRepository idempotencyRecordRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void checkAndRecord(String requestKey) {
        if (requestKey == null || requestKey.trim().isEmpty()) {
            return; // If no key provided, skip idempotency check
        }

        Optional<IdempotencyRecord> existingRecord = idempotencyRecordRepository.findById(requestKey);
        if (existingRecord.isPresent()) {
            throw new IdempotentRequestException("Request with idempotency key " + requestKey + " has already been processed.");
        }

        IdempotencyRecord record = IdempotencyRecord.builder()
                .requestKey(requestKey)
                .build();
        idempotencyRecordRepository.save(record);
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateTransactionId(String requestKey, String transactionId) {
        if (requestKey == null || requestKey.trim().isEmpty()) return;
        
        idempotencyRecordRepository.findById(requestKey).ifPresent(record -> {
            record.setTransactionId(transactionId);
            idempotencyRecordRepository.save(record);
        });
    }
}
