package com.nttdata.currentaccount.Service;

import com.nttdata.currentaccount.Entity.corruntAccountEntity;
import com.nttdata.currentaccount.Transaction.CorruntAccountDto;
import com.nttdata.currentaccount.dto.newCorruntAccountDto;
import org.springframework.stereotype.Service;

@Service
public interface corruntAccountService {


     public corruntAccountEntity findbydni(String dni);

}
