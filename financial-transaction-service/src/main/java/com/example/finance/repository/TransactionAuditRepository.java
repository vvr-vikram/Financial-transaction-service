package com.example.finance.repository;

import com.example.finance.entity.TransactionAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionAuditRepository extends JpaRepository<TransactionAudit, Long> {
}
