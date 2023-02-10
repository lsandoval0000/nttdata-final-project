package com.nttdata.bankaccountsavingsservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_movement")
public class Movement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private OffsetDateTime date;
    private String operation;
    private BigDecimal amount;
    @ManyToOne
    @JoinColumn(name = "savingsAccountId", nullable = false)
    @ToString.Exclude
    private SavingsAccount savingsAccount;
}
