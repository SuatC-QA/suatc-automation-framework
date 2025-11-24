package com.suatc.qa.exceptions;

public class UiElementTimeoutException extends RuntimeException {

    public UiElementTimeoutException(String message) {
        super(message);
    }

    public UiElementTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
