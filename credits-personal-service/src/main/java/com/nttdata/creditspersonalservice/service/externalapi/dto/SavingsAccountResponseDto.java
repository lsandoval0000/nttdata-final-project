package com.nttdata.creditspersonalservice.service.externalapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The savings account response dto.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SavingsAccountResponseDto implements Serializable {
    private SavingsAccountDto savingsAccount;
    private String message;
}
