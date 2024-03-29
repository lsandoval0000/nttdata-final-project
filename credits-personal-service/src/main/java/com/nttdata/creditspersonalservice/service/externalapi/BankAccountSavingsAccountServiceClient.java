package com.nttdata.creditspersonalservice.service.externalapi;

import com.nttdata.creditspersonalservice.service.externalapi.dto.SavingsAccountPaymentInfoDto;
import com.nttdata.creditspersonalservice.service.externalapi.dto.SavingsAccountResponseDto;
import com.nttdata.creditspersonalservice.service.externalapi.error.BankAccountSavingsAccountServiceErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(path = "/api/bank-account", name = "bank-account-savings-service", configuration = {
        BankAccountSavingsAccountServiceErrorDecoder.class })
public interface BankAccountSavingsAccountServiceClient {

    @PostMapping(value = "/savings/{dni}/payment")
    SavingsAccountResponseDto payUsingAccount(
            @PathVariable String dni,
            @RequestBody SavingsAccountPaymentInfoDto paymentInfo);
}
