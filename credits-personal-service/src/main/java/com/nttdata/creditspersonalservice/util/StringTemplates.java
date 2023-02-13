package com.nttdata.creditspersonalservice.util;

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
            "El cliente con DNI ${dni} ${accion} ${monto} soles, el monto restante por "
                    + "pagar del crédito es ${pendiente} soles.";
}
