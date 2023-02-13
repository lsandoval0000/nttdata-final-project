package com.nttdata.creditspersonalservice.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "t_personal_credits")
public class PersonalCredit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credit_id")
    private Long creditId;
    @Column(nullable = false, length = 8)
    private String dni;
    @Column(nullable = false, name = "credit_amount")
    private BigDecimal creditAmount;
    @Column(nullable = false, name = "amount_left")
    private BigDecimal amountLeft;
    @Column(nullable = false, name = "initial_date_to_pay")
    private Date initialDateToPay;
    @Column(nullable = false, name = "months_to_pay")
    private Integer monthsToPay;
    @Column(nullable = false, name = "monthly_fee")
    private BigDecimal monthlyFee;
    @Column(nullable = false, name = "interest_rate")
    private BigDecimal interestRate;
    @Column(nullable = false, name = "late_payment_interest")
    private BigDecimal latePaymentInterest;
    @Column(nullable = false, name = "registered_at", updatable = false)
    @CreationTimestamp
    private OffsetDateTime registeredAt;
    @Column(nullable = false, name = "is_active")
    private Boolean isActive;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "personalCredit")
    @Column(nullable = true)
    @ToString.Exclude
    private List<Transaction> transactions;
}
