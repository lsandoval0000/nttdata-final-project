package com.nttdata.creditspersonalservice.controller.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * The Global exception handler.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException methodArgumentNotValidException,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Errores de validación. Revisar el campo 'errors' para ver los detalles."
        );
        methodArgumentNotValidException.
                getBindingResult().
                getAllErrors().
                forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errorResponse.addValidationError(fieldName, errorMessage);
                });
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleExceptionInternal(
            @NonNull Exception exception,
            Object body,
            @NonNull HttpHeaders httpHeaders,
            @NonNull HttpStatus httpStatus,
            @NonNull WebRequest webRequest) {
        exception.printStackTrace();
        return buildErrorResponse(exception, httpStatus, webRequest);
    }

    /**
     * Handle no such element found exception.
     *
     * @param noSuchElementFoundException the no such element found exception
     * @param webRequest                  the web request
     * @return the response entity
     */
    @ExceptionHandler(NoSuchElementFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNoSuchElementFoundException(
            NoSuchElementFoundException noSuchElementFoundException,
            WebRequest webRequest) {
        noSuchElementFoundException.printStackTrace();
        return buildErrorResponse(noSuchElementFoundException, HttpStatus.NOT_FOUND, webRequest);
    }

    /**
     * Handle unsupported client type exception.
     *
     * @param unsupportedClientTypeException the unsupported client type exception
     * @param webRequest                     the web request
     * @return the response entity
     */
    @ExceptionHandler(UnsupportedClientTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleUnsupportedClientTypeException(
            UnsupportedClientTypeException unsupportedClientTypeException,
            WebRequest webRequest) {
        unsupportedClientTypeException.printStackTrace();
        return buildErrorResponse(unsupportedClientTypeException, HttpStatus.BAD_REQUEST, webRequest);
    }

    /**
     * Handle max value allowed reached exception.
     *
     * @param maxValueAllowedReachedException the max value allowed reached exception
     * @param webRequest                      the web request
     * @return the response entity
     */
    @ExceptionHandler(MaxValueAllowedReachedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMaxValueAllowedReachedException(
            MaxValueAllowedReachedException maxValueAllowedReachedException,
            WebRequest webRequest) {
        maxValueAllowedReachedException.printStackTrace();
        return buildErrorResponse(maxValueAllowedReachedException, HttpStatus.BAD_REQUEST, webRequest);
    }

    /**
     * Handle not enough funds exception.
     *
     * @param notEnoughFundsException the not enough funds exception
     * @param webRequest              the web request
     * @return the response entity
     */
    @ExceptionHandler(NotEnoughFundsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleNotEnoughFundsException(
            NotEnoughFundsException notEnoughFundsException,
            WebRequest webRequest) {
        notEnoughFundsException.printStackTrace();
        return buildErrorResponse(notEnoughFundsException, HttpStatus.BAD_REQUEST, webRequest);
    }

    /**
     * Handle invalid value provided exception.
     *
     * @param invalidValueProvidedException the invalid value provided exception
     * @param webRequest                    the web request
     * @return the response entity
     */
    @ExceptionHandler(InvalidValueProvidedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleInvalidValueProvidedException(
            InvalidValueProvidedException invalidValueProvidedException,
            WebRequest webRequest) {
        invalidValueProvidedException.printStackTrace();
        return buildErrorResponse(invalidValueProvidedException, HttpStatus.BAD_REQUEST, webRequest);
    }

    /**
     * Handle bank account savings account service exception.
     *
     * @param bankAccountSavingsAccountServiceException the bank account savings account service exception
     * @param webRequest                                the web request
     * @return the response entity
     */
    @ExceptionHandler(BankAccountSavingsAccountServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleBankAccountSavingsAccountServiceException(
            BankAccountSavingsAccountServiceException bankAccountSavingsAccountServiceException,
            WebRequest webRequest) {
        bankAccountSavingsAccountServiceException.printStackTrace();
        return buildErrorResponse(bankAccountSavingsAccountServiceException, HttpStatus.BAD_REQUEST, webRequest);
    }

    /**
     * Handle all uncaught generic exception.
     *
     * @param exception  the exception
     * @param webRequest the web request
     * @return the response entity
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(
            Exception exception,
            WebRequest webRequest) {
        exception.printStackTrace();
        return buildErrorResponse(
                exception,
                "Error desconocido ha ocurrido",
                HttpStatus.INTERNAL_SERVER_ERROR,
                webRequest);
    }

    /**
     * Build error response
     *
     * @param exception  the exception
     * @param message    the message
     * @param httpStatus the http status
     * @param webRequest the web request
     * @return the response entity
     */
    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            String message,
            HttpStatus httpStatus,
            WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    /**
     * Build error response
     *
     * @param exception  the exception
     * @param httpStatus the http status
     * @param webRequest the web request
     * @return the response entity
     */
    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            HttpStatus httpStatus,
            WebRequest webRequest) {
        return buildErrorResponse(exception, exception.getMessage(), httpStatus, webRequest);
    }
}
