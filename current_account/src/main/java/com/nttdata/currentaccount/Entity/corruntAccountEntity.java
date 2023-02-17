package com.nttdata.currentaccount.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "corruntaccount")
public class corruntAccountEntity implements Serializable {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_corrunt_account")

    private String dni;
    private BigDecimal maintenance;
    private String description;
    private int monthly_available;
    private String ruc;
    private String addres;

}