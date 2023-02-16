package com.nttdata.creditspersonalservice.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
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
    private String description;
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;
    @ManyToOne
    @JoinColumn(name = "credit_id", nullable = false)
    @ToString.Exclude
    private PersonalCredit personalCredit;
}
