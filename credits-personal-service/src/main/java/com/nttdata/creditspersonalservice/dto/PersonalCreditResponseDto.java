package com.nttdata.creditspersonalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The personal credit response dto.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonalCreditResponseDto implements Serializable {
    private PersonalCreditDto personalCredit;
    private String message;
}
