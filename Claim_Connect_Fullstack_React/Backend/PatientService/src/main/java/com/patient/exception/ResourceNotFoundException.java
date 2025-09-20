package com.patient.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super("Patient not found with given id!");
    }
    public ResourceNotFoundException(String msg) {
    	super(msg);
    }
}
