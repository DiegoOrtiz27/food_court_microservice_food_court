package com.foodquart.microservicefoodcourt.domain.exception;

public class NitAlreadyExistsException extends DomainException {
    public NitAlreadyExistsException(String nit) {
        super("The NIT '" + nit + "' is already registered");
    }
}
