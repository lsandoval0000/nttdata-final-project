package com.nttdata.bankaccountsavingsservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * The general savings account config entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class GeneralSavingsAccountConfigEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer monthlyAvailableMovements;
    private BigDecimal maintenanceFee;
    private BigDecimal feeForOperation;
}
