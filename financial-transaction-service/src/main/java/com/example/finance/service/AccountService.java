package com.example.finance.service;

import com.example.finance.dto.AccountRequestDTO;
import com.example.finance.dto.AccountResponseDTO;
import com.example.finance.entity.Account;
import com.example.finance.entity.AccountStatus;
import com.example.finance.exception.BusinessException;
import com.example.finance.exception.ResourceNotFoundException;
import com.example.finance.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public AccountResponseDTO createAccount(AccountRequestDTO request) {
        if (accountRepository.findByAccountNumber(request.getAccountNumber()).isPresent()) {
            throw new BusinessException("Account number already exists.");
        }

        Account account = Account.builder()
                .accountHolderName(request.getAccountHolderName())
                .accountNumber(request.getAccountNumber())
                .balance(request.getInitialBalance())
                .status(AccountStatus.ACTIVE)
                .build();

        Account savedAccount = accountRepository.save(account);
        return mapToDTO(savedAccount);
    }

    public AccountResponseDTO getAccountDetails(String id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        return mapToDTO(account);
    }

    private AccountResponseDTO mapToDTO(Account account) {
        return AccountResponseDTO.builder()
                .accountId(account.getAccountId())
                .accountHolderName(account.getAccountHolderName())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .status(account.getStatus())
                .createdAt(account.getCreatedAt())
                .build();
    }
}
