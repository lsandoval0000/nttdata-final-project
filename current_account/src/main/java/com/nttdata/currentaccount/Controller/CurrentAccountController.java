package com.nttdata.currentaccount.Controller;

import com.nttdata.currentaccount.Entity.corruntAccountEntity;
import com.nttdata.currentaccount.Service.corruntAccountService;
import com.nttdata.currentaccount.dto.newCorruntAccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/current_account")
public class CurrentAccountController {

    private final corruntAccountService CorruntAccountService;

    @GetMapping("/Search/{dni}")
    public corruntAccountEntity creditCardsByDni(@PathVariable String dni) {
        return CorruntAccountService.findbydni(dni);
    }


}
