package com.nttdata.currentaccount.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CurrentAccountDto implements Serializable {

    private String dni;
    private BigDecimal maintenance;
    private String description;
    private int monthly_available;
    private String ruc;
    private String addres;
}
