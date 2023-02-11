package com.nttdata.bankaccountsavingsservice.util;

import java.math.BigDecimal;

/**
 * Default values.
 */
public final class DefaultValues {
    /**
     * Monthly available movements.
     */
    public static final Integer MONTHLY_AVAILABLE_MOVEMENTS = 5;
    /**
     * Maintenance fee.
     */
    public static final BigDecimal MAINTENANCE_FEE = BigDecimal.valueOf(0L);
    /**
     * Fee per operation.
     */
    public static final BigDecimal FEE_PER_OPERATION = BigDecimal.valueOf(4.0D);

    private DefaultValues() {

    }
}
