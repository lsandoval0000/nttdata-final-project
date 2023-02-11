package com.nttdata.bankaccountsavingsservice.controller.exception;

/**
 * The not enough funds exception.
 */
public class NotEnoughFundsException extends RuntimeException {
    /**
     * Instantiates a new Not enough funds exception.
     *
     * @param message the message
     */
    public NotEnoughFundsException(String message) {
        super(message);
    }
}
