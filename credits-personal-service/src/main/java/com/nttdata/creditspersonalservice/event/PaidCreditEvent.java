package com.nttdata.creditspersonalservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaidCreditEvent {
    private String transactionId;
}
