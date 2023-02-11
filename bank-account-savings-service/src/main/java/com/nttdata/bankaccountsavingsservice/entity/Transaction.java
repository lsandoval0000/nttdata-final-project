package com.nttdata.bankaccountsavingsservice.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "transaction_date", nullable = false, updatable = false)
    @CreationTimestamp
    private OffsetDateTime transactionDate;
    @Column(nullable = false)
    private String transaction;
    @Column(nullable = false)
    private BigDecimal amount;
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    @ToString.Exclude
    private SavingsAccount savingsAccount;
}
