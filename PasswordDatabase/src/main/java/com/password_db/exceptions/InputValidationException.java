package com.password_db.exceptions;

public class InputValidationException extends Exception {
    public InputValidationException(String errorMessage){
        super(errorMessage);
    }

    public InputValidationException(String errorMessage, Throwable err){
        super(errorMessage, err);
    }
}
