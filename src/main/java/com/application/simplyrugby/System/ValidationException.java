package com.application.simplyrugby.System;
/**
 * Custom Exception class to create error messages used by the app.
 */
public class ValidationException extends Exception{
    public ValidationException(String message){
        super(message);
    }
}
