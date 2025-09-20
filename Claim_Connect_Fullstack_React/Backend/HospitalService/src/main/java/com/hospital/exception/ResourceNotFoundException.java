package com.hospital.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super("Hospital not found with given id");
    }
    
    public ResourceNotFoundException(String msg) {
        super(msg);
    }
}
