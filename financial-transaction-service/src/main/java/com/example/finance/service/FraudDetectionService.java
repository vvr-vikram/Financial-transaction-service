package com.example.finance.service;

import com.example.finance.entity.FraudRule;
import com.example.finance.exception.BusinessException;
import com.example.finance.repository.FraudRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FraudDetectionService {

    private final FraudRuleRepository fraudRuleRepository;

    public void validateTransaction(BigDecimal amount) {
        Optional<FraudRule> maxTxnRule = fraudRuleRepository.findByRuleName("MAX_TXN_AMOUNT");
        if (maxTxnRule.isPresent() && maxTxnRule.get().getEnabled()) {
            if (amount.compareTo(maxTxnRule.get().getMaxAmountLimit()) > 0) {
                throw new BusinessException("Transaction failed: Amount exceeds maximum allowed limit for a single transaction.");
            }
        }
      
    }
}
