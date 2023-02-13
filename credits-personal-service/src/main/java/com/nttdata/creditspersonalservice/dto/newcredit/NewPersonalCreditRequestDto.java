package com.nttdata.creditspersonalservice.dto.newcredit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewPersonalCreditRequestDto implements Serializable {
    @NotBlank(message = "Dni es requerido.")
    private String dni;
    @NotBlank(message = "El tipo de cliente es requerido.")
    private String clientType;
    @DecimalMin(value = "1.0", message = "El monto de crédito debe ser mayor a cero.")
    @Digits(integer = 10, fraction = 2, message = "El monto de crédito debe tener como máximo dos decimales.")
    private BigDecimal creditAmount;
    @NotNull(message = "Se requiere la fecha en que se define el día del pago.")
    private Date initialDateToPay;
    @DecimalMin(value = "1", message = "El número de meses debe ser mayor a cero.")
    @Digits(integer = 10, fraction = 0, message = "El número de meses es un número entero positivo.")
    private Integer monthsToPay;
    @DecimalMin(value = "1.0", message = "La cuota mensual debe ser mayor a cero.")
    @Digits(integer = 10, fraction = 2, message = "La cuota mensual debe tener como máximo dos decimales.")
    private BigDecimal monthlyFee;
    @DecimalMin(value = "0.01", message = "Tasa de interés debe ser mayor a cero.")
    @Digits(integer = 10, fraction = 2, message = "Tasa de interés debe tener como máximo dos decimales.")
    private BigDecimal interestRate;
    @DecimalMin(value = "0.01", message = "Interés por mora debe ser mayor a cero.")
    @Digits(integer = 10, fraction = 2, message = "Interés por mora debe tener como máximo dos decimales.")
    private BigDecimal latePaymentInterest;
}
