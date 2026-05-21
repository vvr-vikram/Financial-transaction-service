package com.example.finance.controller;

import com.example.finance.dto.AccountRequestDTO;
import com.example.finance.dto.AccountResponseDTO;
import com.example.finance.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Account Management", description = "APIs for managing accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @Operation(summary = "Create a new account")
    public ResponseEntity<AccountResponseDTO> createAccount(@Valid @RequestBody AccountRequestDTO request) {
        return new ResponseEntity<>(accountService.createAccount(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account details by ID")
    public ResponseEntity<AccountResponseDTO> getAccount(@PathVariable String id) {
        return ResponseEntity.ok(accountService.getAccountDetails(id));
    }
}
