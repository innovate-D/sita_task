package org.sita.exception;

import lombok.experimental.StandardException;

@StandardException
public class CustomException extends RuntimeException{

    public CustomException(String message){
        super(message);
    }
}
