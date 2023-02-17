package com.nttdata.customer.exception;

public class NoSuchMethodException extends ReflectiveOperationException {
    //constructors

    /**
     *
     */
    public NoSuchMethodException() {
        super();
    }

    public NoSuchMethodException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param cause
     */

    public NoSuchMethodException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchMethodException(Throwable cause) {
        super(cause);
    }
}
