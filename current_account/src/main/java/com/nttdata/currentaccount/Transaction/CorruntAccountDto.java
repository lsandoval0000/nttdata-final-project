package com.nttdata.currentaccount.Transaction;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

public class CorruntAccountDto implements Serializable {
    private String dni;
    private String ruc;
    private String description;
    private BigDecimal maintenance;
    private Integer monthly_available;
    private String adress;

}
