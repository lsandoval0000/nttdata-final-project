package com.nttdata.creditspersonalservice.dto.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Base mapper.
 *
 * @param <E> the entity
 * @param <D> the dto
 */
public abstract class BaseMapper<E, D> {
    /**
     * Convert to entity.
     *
     * @param dto  the dto
     * @param args the args
     * @return the entity
     */
    public abstract E convertToEntity(D dto, Object... args);

    /**
     * Convert to dto.
     *
     * @param entity the entity
     * @param args   the args
     * @return the dto
     */
    public abstract D convertToDto(E entity, Object... args);

    /**
     * Convert to entity collection.
     *
     * @param dto  the dto
     * @param args the args
     * @return the collection
     */
    public Collection<E> convertToEntity(Collection<D> dto, Object... args) {
        return dto.stream().map(d -> convertToEntity(d, args)).collect(Collectors.toList());
    }

    /**
     * Convert to dto collection.
     *
     * @param entity the entity
     * @param args   the args
     * @return the collection
     */
    public Collection<D> convertToDto(Collection<E> entity, Object... args) {
        return entity.stream().map(e -> convertToDto(e, args)).collect(Collectors.toList());
    }

    /**
     * Convert to entity list.
     *
     * @param dto  the dto
     * @param args the args
     * @return the list
     */
    public List<E> convertToEntityList(Collection<D> dto, Object... args) {
        return convertToEntity(dto, args).stream().collect(Collectors.toList());
    }

    /**
     * Convert to dto list.
     *
     * @param entity the entity
     * @param args   the args
     * @return the list
     */
    public List<D> convertToDtoList(Collection<E> entity, Object... args) {
        return convertToDto(entity, args).stream().collect(Collectors.toList());
    }
}
