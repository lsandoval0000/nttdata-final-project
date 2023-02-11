package com.nttdata.bankaccountsavingsservice.controller.exception;

/**
 * The max value allowed reached.
 */
public class MaxValueAllowedReachedException extends RuntimeException {
    /**
     * Instantiates a new Max value allowed reached.
     *
     * @param message the message
     */
    public MaxValueAllowedReachedException(String message) {
        super(message);
    }
}
