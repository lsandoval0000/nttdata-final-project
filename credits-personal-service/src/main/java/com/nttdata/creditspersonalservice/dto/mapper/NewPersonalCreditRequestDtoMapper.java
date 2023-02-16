package com.nttdata.creditspersonalservice.dto.mapper;

import com.nttdata.creditspersonalservice.dto.newcredit.NewPersonalCreditRequestDto;
import com.nttdata.creditspersonalservice.entity.PersonalCredit;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * The new personal credit request dto mapper.
 */
@Component
public class NewPersonalCreditRequestDtoMapper extends BaseMapper<PersonalCredit, NewPersonalCreditRequestDto> {
    /**
     * @param dto  the dto
     * @param args the args
     * @return the entity
     */
    @Override
    public PersonalCredit convertToEntity(NewPersonalCreditRequestDto dto, Object... args) {
        PersonalCredit entity = new PersonalCredit();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);
        }
        entity.setIsActive(true);
        entity.setAmountLeft(entity.getCreditAmount());
        return entity;
    }

    /**
     * @param entity the entity
     * @param args   the args
     * @return the dto
     */
    @Override
    public NewPersonalCreditRequestDto convertToDto(PersonalCredit entity, Object... args) {
        NewPersonalCreditRequestDto dto = new NewPersonalCreditRequestDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }
}
