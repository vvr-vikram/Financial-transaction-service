package com.example.finance.dto;

import com.example.finance.entity.TransactionStatus;
import com.example.finance.entity.TransactionType;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponseDTO {
    private String transactionId;
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
    private String currency;
    private TransactionType type;
    private TransactionStatus status;
    private LocalDateTime createdAt;
}
