package com.password_db.exceptions;

public class NoPasswordException extends Exception{
    public NoPasswordException(String errorMessage){
        super(errorMessage);
    }

    public NoPasswordException(String errorMessage, Throwable err){
        super(errorMessage, err);
    }
}
