package com.nttdata.creditspersonalservice.dto.mapper;

import com.nttdata.creditspersonalservice.dto.PersonalCreditDto;
import com.nttdata.creditspersonalservice.entity.PersonalCredit;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * The personal credit dto mapper.
 */
@Component
public class PersonalCreditDtoMapper extends BaseMapper<PersonalCredit, PersonalCreditDto> {
    /**
     * @param dto  the dto
     * @param args the args
     * @return the entity
     */
    @Override
    public PersonalCredit convertToEntity(PersonalCreditDto dto, Object... args) {
        PersonalCredit entity = new PersonalCredit();
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
    public PersonalCreditDto convertToDto(PersonalCredit entity, Object... args) {
        PersonalCreditDto dto = new PersonalCreditDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }
}
