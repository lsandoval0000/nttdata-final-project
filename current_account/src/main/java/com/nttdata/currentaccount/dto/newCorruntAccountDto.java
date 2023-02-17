package com.nttdata.currentaccount.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class newCorruntAccountDto implements Serializable {

    @NotBlank(message = "Ingrese el DNI ")
    private String dni;

    @NotBlank(message = "Ingrese el RUC")
    private String ruc;

    @NotBlank(message = "Ingrese la descripcion obligatoria ")
    private String description;

    @DecimalMin(value="0.50" , message = "Cobro por mantenimiento mensual")
    private BigDecimal maintenance;

    @NotBlank(message = "Ingrese el deposito mensual ")
    private Integer monthly_available;

    @NotBlank(message = "Ingrese la direccion de domicilio")
    private String adress;

}




