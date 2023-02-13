package com.nttdata.creditspersonalservice.controller.exception;

/**
 * The unsupported client type exception.
 */
public class UnsupportedClientTypeException extends RuntimeException {
    /**
     * Instantiates a new Unsupported client type exception.
     *
     * @param msg the msg
     */
    public UnsupportedClientTypeException(String msg) {
        super(msg);
    }
}
