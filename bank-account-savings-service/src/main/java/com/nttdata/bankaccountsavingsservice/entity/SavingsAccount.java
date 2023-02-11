package com.nttdata.bankaccountsavingsservice.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "t_savings_account")
public class SavingsAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;
    @Column(unique = true, nullable = false, name = "account_number", length = 16)
    private String accountNumber;
    @Column(nullable = false, length = 8)
    private String dni;
    @Column(nullable = false)
    private BigDecimal balance;
    @Column(nullable = false, name = "monthly_available_movements")
    private Integer monthlyAvailableMovements;
    @Column(nullable = false, name = "maintenance_fee")
    private BigDecimal maintenanceFee;
    @Column(nullable = false, name = "created_at", updatable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "savingsAccount")
    @ToString.Exclude
    private List<Transaction> transactions;
}
