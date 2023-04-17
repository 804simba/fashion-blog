package com.timolisa.fashionblogapi.exception;

public class InvalidInputsException extends Exception {
    public InvalidInputsException() {
        super();
    }

    public InvalidInputsException(String message) {
        super(message);
    }

    public InvalidInputsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidInputsException(Throwable cause) {
        super(cause);
    }
}
