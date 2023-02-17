package com.nttdata.customer.exception;

public class CustomerException extends RuntimeException {


    /**
     *
     * @param message
     */
    public CustomerException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public CustomerException(String message, Throwable cause) {
        super(message, cause);
    }
}
