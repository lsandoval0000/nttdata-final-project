package com.nttdata.creditcard.entity;

import com.nttdata.creditcard.util.TransactionAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The Transaction.
 */
@Entity
@Table(name="transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String date;
    @Column(nullable = false, length = 100)
    private String description;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private TransactionAction action;
    @ManyToOne
    @JoinColumn(name = "credit_card_id", nullable = false)
    private CreditCard creditCard;
}
