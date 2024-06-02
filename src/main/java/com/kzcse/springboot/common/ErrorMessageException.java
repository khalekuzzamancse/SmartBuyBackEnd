package com.kzcse.springboot.common;

public class ErrorMessageException extends Exception {
    private ErrorMessage errorMessage;

    public ErrorMessageException(ErrorMessage errorMessage) {
        super(errorMessage.toString()); // Pass the error message to the Exception constructor
        this.errorMessage = errorMessage;
    }

    public ErrorMessageException(String message, ErrorMessage errorMessage) {
        super(message);
        this.errorMessage = errorMessage;
    }

    public ErrorMessage get() {
        return errorMessage;
    }
}
