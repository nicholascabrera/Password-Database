package com.password_db.exceptions;

public class IncorrectUsernameException extends Exception {

    public IncorrectUsernameException(String errorMessage){
        super(errorMessage);
    }

    public IncorrectUsernameException(String errorMessage, Throwable err){
        super(errorMessage, err);
    }
}