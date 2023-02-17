package com.nttdata.currentaccount.Service;

import com.nttdata.currentaccount.Entity.corruntAccountEntity;
import com.nttdata.currentaccount.Repository.corruntAccountRepository;
import com.nttdata.currentaccount.Transaction.CorruntAccountDto;
import com.nttdata.currentaccount.dto.newCorruntAccountDto;
import org.springframework.beans.factory.annotation.Autowired;

public class CorruntAccountServiceImpl implements corruntAccountService{
@Autowired
    corruntAccountRepository corruntAccountRepository;

    @Override
    public corruntAccountEntity findbydni(String dni) {
     return corruntAccountRepository.findByDni(dni).orElse(null);
    }
}
