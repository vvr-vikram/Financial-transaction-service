package com.example.finance.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferRequestDTO {
    @NotBlank(message = "From Account ID is mandatory")
    private String fromAccountId;

    @NotBlank(message = "To Account ID is mandatory")
    private String toAccountId;

    @NotNull(message = "Amount is mandatory")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "Currency is mandatory")
    private String currency = "USD";
}
