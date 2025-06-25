package org.sita.exception;

import lombok.experimental.StandardException;

import java.util.UUID;

@StandardException
public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException(UUID id){
        super("Order with id "+id+" not found");
    }
}
