package com.nttdata.creditspersonalservice.service.externalapi;

import com.nttdata.creditspersonalservice.service.externalapi.dto.SavingsAccountPaymentInfoDto;
import com.nttdata.creditspersonalservice.service.externalapi.dto.SavingsAccountResponseDto;
import com.nttdata.creditspersonalservice.service.externalapi.error.BankAccountSavingsAccountServiceErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "bank-account-savings-service", configuration = {BankAccountSavingsAccountServiceErrorDecoder.class})
public interface BankAccountSavingsAccountServiceClient {

    @PostMapping(value = "/api/bank-account/savings/{dni}/paid")
    SavingsAccountResponseDto payUsingAccount(
            @PathVariable String dni,
            @RequestBody SavingsAccountPaymentInfoDto paymentInfo);
}
