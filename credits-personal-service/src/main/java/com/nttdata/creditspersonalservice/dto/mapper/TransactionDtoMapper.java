package com.nttdata.creditspersonalservice.dto.mapper;


import com.nttdata.creditspersonalservice.dto.transaction.TransactionDto;
import com.nttdata.creditspersonalservice.entity.Transaction;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * The transaction dto mapper.
 */
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
