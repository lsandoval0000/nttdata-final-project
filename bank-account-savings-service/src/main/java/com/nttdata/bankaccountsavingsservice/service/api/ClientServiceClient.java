package com.nttdata.bankaccountsavingsservice.service.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "client-service")
public interface ClientServiceClient {

    // TODO consultar la estructura de este cliente

    @GetMapping(value = "/test/tes")
    Boolean clientExists(@PathVariable String dni);
}
