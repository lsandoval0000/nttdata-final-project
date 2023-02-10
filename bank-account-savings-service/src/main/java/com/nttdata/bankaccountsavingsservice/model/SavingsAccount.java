package com.nttdata.bankaccountsavingsservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_savings_account")
public class SavingsAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    @Column(unique = true)
    private String accountNumber;
    private String dni;
    private BigDecimal balance;
    private Integer monthlyAvailableMovements;
    private BigDecimal maintenanceFee;
    private OffsetDateTime creationDate;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "savingsAccount")
    @ToString.Exclude
    private List<Movement> movements;
}
