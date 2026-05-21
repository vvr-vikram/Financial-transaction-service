package com.example.finance.service;

import com.example.finance.dto.TransactionRequestDTO;
import com.example.finance.dto.TransactionResponseDTO;
import com.example.finance.dto.TransferRequestDTO;
import com.example.finance.entity.Account;
import com.example.finance.entity.AccountStatus;
import com.example.finance.entity.Transaction;
import com.example.finance.entity.TransactionStatus;
import com.example.finance.entity.TransactionType;
import com.example.finance.exception.BusinessException;
import com.example.finance.exception.ResourceNotFoundException;
import com.example.finance.repository.AccountRepository;
import com.example.finance.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final FraudDetectionService fraudDetectionService;
    private final AuditService auditService;
    private final IdempotencyService idempotencyService;

    @Transactional
    public TransactionResponseDTO processDebit(TransactionRequestDTO request, String requestKey) {
        idempotencyService.checkAndRecord(requestKey);
        fraudDetectionService.validateTransaction(request.getAmount());

        Account account = getActiveAccount(request.getAccountId());
        
        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new BusinessException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .fromAccount(account.getAccountId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .type(TransactionType.DEBIT)
                .status(TransactionStatus.SUCCESS)
                .build();
        
        transaction = transactionRepository.save(transaction);
        auditService.logAudit(transaction.getTransactionId(), "DEBIT_SUCCESS", "Successfully debited amount " + request.getAmount());
        idempotencyService.updateTransactionId(requestKey, transaction.getTransactionId());

        return mapToDTO(transaction);
    }

    @Transactional
    public TransactionResponseDTO processCredit(TransactionRequestDTO request, String requestKey) {
        idempotencyService.checkAndRecord(requestKey);
        fraudDetectionService.validateTransaction(request.getAmount());

        Account account = getActiveAccount(request.getAccountId());
        
        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .toAccount(account.getAccountId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .type(TransactionType.CREDIT)
                .status(TransactionStatus.SUCCESS)
                .build();
        
        transaction = transactionRepository.save(transaction);
        auditService.logAudit(transaction.getTransactionId(), "CREDIT_SUCCESS", "Successfully credited amount " + request.getAmount());
        idempotencyService.updateTransactionId(requestKey, transaction.getTransactionId());

        return mapToDTO(transaction);
    }

    @Transactional
    public TransactionResponseDTO processTransfer(TransferRequestDTO request, String requestKey) {
        idempotencyService.checkAndRecord(requestKey);
        fraudDetectionService.validateTransaction(request.getAmount());

        if (request.getFromAccountId().equals(request.getToAccountId())) {
            throw new BusinessException("Cannot transfer to the same account");
        }

        Account fromAccount = getActiveAccount(request.getFromAccountId());
        Account toAccount = getActiveAccount(request.getToAccountId());

        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new BusinessException("Insufficient balance in source account");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = Transaction.builder()
                .fromAccount(fromAccount.getAccountId())
                .toAccount(toAccount.getAccountId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.SUCCESS)
                .build();
        
        transaction = transactionRepository.save(transaction);
        auditService.logAudit(transaction.getTransactionId(), "TRANSFER_SUCCESS", "Successfully transferred amount " + request.getAmount());
        idempotencyService.updateTransactionId(requestKey, transaction.getTransactionId());

        return mapToDTO(transaction);
    }

    public TransactionResponseDTO getTransaction(String transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        return mapToDTO(transaction);
    }

    public Page<TransactionResponseDTO> getTransactionHistory(Pageable pageable) {
        return transactionRepository.findAll(pageable).map(this::mapToDTO);
    }

    private Account getActiveAccount(String accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new BusinessException("Account is not active");
        }
        return account;
    }

    private TransactionResponseDTO mapToDTO(Transaction transaction) {
        return TransactionResponseDTO.builder()
                .transactionId(transaction.getTransactionId())
                .fromAccount(transaction.getFromAccount())
                .toAccount(transaction.getToAccount())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .type(transaction.getType())
                .status(transaction.getStatus())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
