package com.nttdata.creditspersonalservice.util;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Enum validator.
 */
public class EnumValidator {
    private final Set<String> values;

    private EnumValidator(Set<String> values) {
        this.values = values;
    }

    /**
     * Of enum validator.
     *
     * @param <T>      the type parameter
     * @param enumType the enum type
     * @return the enum validator
     */
    public static <T extends Enum<T>> EnumValidator of(Class<T> enumType) {
        return new EnumValidator(
                Stream.of(enumType.getEnumConstants())
                        .map(Enum::name)
                        .collect(Collectors.toSet()));
    }

    /**
     * Is present boolean.
     *
     * @param value the value
     * @return boolean
     */
    public boolean isPresent(String value) {
        return values.contains(value);
    }
}
