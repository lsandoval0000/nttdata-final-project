package com.nttdata.bankaccountsavingsservice.util;

/**
 * String templates.
 */
public class StringTemplates {

    private StringTemplates() {
    }

    /**
     * The constant TRANSACTION_DESCRIPTION_TEMPLATE.
     */
    public static final String TRANSACTION_DESCRIPTION_TEMPLATE =
            "El cliente con DNI ${dni} ${accion} ${monto} soles, el nuevo balance de la cuenta es ${balance} soles.";
}
