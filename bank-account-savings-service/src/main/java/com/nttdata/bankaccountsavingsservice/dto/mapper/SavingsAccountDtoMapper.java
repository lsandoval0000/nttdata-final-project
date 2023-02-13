package com.nttdata.bankaccountsavingsservice.dto.mapper;

import com.nttdata.bankaccountsavingsservice.dto.SavingsAccountDto;
import com.nttdata.bankaccountsavingsservice.entity.SavingsAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * The Savings account dto mapper.
 */
@Component
public class SavingsAccountDtoMapper extends BaseMapper<SavingsAccount, SavingsAccountDto> {
    /**
     * @param dto  the dto
     * @param args the args
     * @return the entity
     */
    @Override
    public SavingsAccount convertToEntity(SavingsAccountDto dto, Object... args) {
        SavingsAccount entity = new SavingsAccount();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);
        }
        return entity;
    }

    /**
     * @param entity the entity
     * @param args   the args
     * @return the dto
     */
    @Override
    public SavingsAccountDto convertToDto(SavingsAccount entity, Object... args) {
        SavingsAccountDto dto = new SavingsAccountDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }
}
