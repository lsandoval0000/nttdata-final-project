package com.nttdata.bankaccountsavingsservice.dto.newaccount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The savings account request dto.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewSavingsAccountRequestDto implements Serializable {
    @NotBlank(message = "Dni es requerido.")
    private String dni;
    @NotBlank(message = "El tipo de cliente es requerido.")
    private String clientType;
    @DecimalMin(value = "0.0", message = "El monto inicial debe ser mayor o igual a cero.")
    @Digits(integer = 15, fraction = 2, message = "El monto inicial debe tener como máximo dos decimales.")
    private BigDecimal initialAmount;
}
