package com.nttdata.bankaccountsavingsservice.controller.exception;

/**
 * The No such element found exception.
 */
public class NoSuchElementFoundException extends RuntimeException {
    /**
     * Instantiates a new No such element found exception.
     *
     * @param messsage the messsage
     */
    public NoSuchElementFoundException(String messsage) {
        super(messsage);
    }
}
