package com.nttdata.bankaccountsavingsservice.dto.mapper;

import com.nttdata.bankaccountsavingsservice.dto.transaction.TransactionDto;
import com.nttdata.bankaccountsavingsservice.entity.Transaction;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class TransactionDtoMapper extends BaseMapper<Transaction, TransactionDto> {
    /**
     * @param dto  the dto
     * @param args the args
     * @return entity
     */
    @Override
    public Transaction convertToEntity(TransactionDto dto, Object... args) {
        Transaction entity = new Transaction();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);
        }
        return entity;
    }

    /**
     * @param entity the entity
     * @param args   the args
     * @return dto
     */
    @Override
    public TransactionDto convertToDto(Transaction entity, Object... args) {
        TransactionDto dto = new TransactionDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }
}
