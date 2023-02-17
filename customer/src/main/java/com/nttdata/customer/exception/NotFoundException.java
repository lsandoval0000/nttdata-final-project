package com.nttdata.customer.exception;

public class NotFoundException extends RuntimeException {

    /**
     *
     * @param message
     */
    public NotFoundException(String message) {
        super(message);
    }
}