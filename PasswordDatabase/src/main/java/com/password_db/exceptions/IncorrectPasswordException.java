package com.password_db.exceptions;

public class IncorrectPasswordException extends Exception {

    public IncorrectPasswordException(String errorMessage){
        super(errorMessage);
    }

    public IncorrectPasswordException(String errorMessage, Throwable err){
        super(errorMessage, err);
    }
}