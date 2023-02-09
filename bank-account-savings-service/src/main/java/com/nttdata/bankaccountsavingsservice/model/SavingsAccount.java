package com.nttdata.bankaccountsavingsservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
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
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Movement> movements;
}
