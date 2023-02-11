package com.nttdata.bankaccountsavingsservice.service.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "client-service")
public interface ClientServiceClient {

    // TODO consultar la estructura de este cliente

    @RequestMapping(method = RequestMethod.GET, value = "")
    public Boolean clientExists(@PathVariable String dni);
}
