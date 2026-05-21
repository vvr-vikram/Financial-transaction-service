package com.example.finance.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "fraud_rule")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FraudRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    private Long ruleId;

    @Column(name = "rule_name", nullable = false, unique = true)
    private String ruleName;

    @Column(name = "max_amount_limit")
    private BigDecimal maxAmountLimit;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;
}
