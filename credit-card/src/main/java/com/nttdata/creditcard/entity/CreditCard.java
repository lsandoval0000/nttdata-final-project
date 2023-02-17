package com.nttdata.creditcard.entity;

import com.nttdata.creditcard.util.CreditCardStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * The Credit card.
 */
@Entity
@Table(name = "credit_card")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="account_number", length = 16, nullable = false, unique = true)
    private String accountNumber;
    @Column(name="card_number", length = 16, nullable = false, unique = true)
    private String cardNumber;
    @Column(name="credit_limit", nullable = false)
    private BigDecimal creditLimit;
    @Column(name="amount_consumed", nullable = false)
    private BigDecimal amountConsumed;
    @Column(name="status", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private CreditCardStatus status;
    @Column(name="dni", length = 8, nullable = false)
    private String dni;
}
