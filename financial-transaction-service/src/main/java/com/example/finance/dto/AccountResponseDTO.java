package com.example.finance.dto;

import com.example.finance.entity.AccountStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AccountResponseDTO {
    private String accountId;
    private String accountHolderName;
    private String accountNumber;
    private BigDecimal balance;
    private AccountStatus status;
    private LocalDateTime createdAt;
}
