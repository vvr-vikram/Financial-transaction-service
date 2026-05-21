package com.example.finance.controller;

import com.example.finance.dto.TransactionRequestDTO;
import com.example.finance.dto.TransactionResponseDTO;
import com.example.finance.dto.TransferRequestDTO;
import com.example.finance.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction Processing", description = "APIs for financial transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/debit")
    @Operation(summary = "Debit money from an account")
    public ResponseEntity<TransactionResponseDTO> debit(
            @Valid @RequestBody TransactionRequestDTO request,
            @RequestHeader(value = "Idempotency-Key", required = false) @Parameter(description = "Unique key for idempotency") String idempotencyKey) {
        return ResponseEntity.ok(transactionService.processDebit(request, idempotencyKey));
    }

    @PostMapping("/credit")
    @Operation(summary = "Credit money to an account")
    public ResponseEntity<TransactionResponseDTO> credit(
            @Valid @RequestBody TransactionRequestDTO request,
            @RequestHeader(value = "Idempotency-Key", required = false) @Parameter(description = "Unique key for idempotency") String idempotencyKey) {
        return ResponseEntity.ok(transactionService.processCredit(request, idempotencyKey));
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transfer money between two accounts")
    public ResponseEntity<TransactionResponseDTO> transfer(
            @Valid @RequestBody TransferRequestDTO request,
            @RequestHeader(value = "Idempotency-Key", required = false) @Parameter(description = "Unique key for idempotency") String idempotencyKey) {
        return ResponseEntity.ok(transactionService.processTransfer(request, idempotencyKey));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction details by ID")
    public ResponseEntity<TransactionResponseDTO> getTransaction(@PathVariable String id) {
        return ResponseEntity.ok(transactionService.getTransaction(id));
    }

    @GetMapping("/history")
    @Operation(summary = "Get transaction history with pagination")
    public ResponseEntity<Page<TransactionResponseDTO>> getTransactionHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(transactionService.getTransactionHistory(pageable));
    }
}
