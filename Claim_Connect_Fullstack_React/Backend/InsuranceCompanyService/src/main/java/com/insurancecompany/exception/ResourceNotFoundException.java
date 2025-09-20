package com.insurancecompany.exception;

public class ResourceNotFoundException extends RuntimeException {
	
	public ResourceNotFoundException() {
        super("InsuranceCompany not found given id!");
    }
	
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

