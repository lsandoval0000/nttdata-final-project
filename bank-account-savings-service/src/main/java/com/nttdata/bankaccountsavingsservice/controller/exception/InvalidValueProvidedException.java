package com.nttdata.bankaccountsavingsservice.controller.exception;

/**
 * The invalid value provided exception.
 */
public class InvalidValueProvidedException extends RuntimeException {
    /**
     * Instantiates a new Invalid value provided exception.
     *
     * @param message the message
     */
    public InvalidValueProvidedException(String message) {
        super(message);
    }
}
