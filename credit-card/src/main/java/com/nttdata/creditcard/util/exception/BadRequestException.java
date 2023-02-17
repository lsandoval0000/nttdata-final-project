package com.nttdata.creditcard.util.exception;

/**
 * The type Bad request exception.
 */
public class BadRequestException extends RuntimeException{
    /**
     * Instantiates a new Bad request exception.
     *
     * @param message the message
     */
    public BadRequestException(String message) {
        super(message);
    }
}
