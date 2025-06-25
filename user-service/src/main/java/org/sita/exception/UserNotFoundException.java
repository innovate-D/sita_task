package org.sita.exception;

import lombok.experimental.StandardException;

import java.util.UUID;

@StandardException
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(UUID id){
        super("User with id "+id+" not found");
    }
}
