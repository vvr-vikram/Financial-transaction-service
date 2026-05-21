package com.example.finance.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class AccountRequestDTO {
    @NotBlank(message = "Account holder name is mandatory")
    private String accountHolderName;

    @NotBlank(message = "Account number is mandatory")
    private String accountNumber;

    @NotNull(message = "Initial balance is mandatory")
    @DecimalMin(value = "0.0", inclusive = true, message = "Initial balance cannot be negative")
    private BigDecimal initialBalance;
}
