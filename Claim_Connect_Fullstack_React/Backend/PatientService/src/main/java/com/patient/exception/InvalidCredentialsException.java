package com.patient.exception;

public class InvalidCredentialsException  extends RuntimeException{
	
	public InvalidCredentialsException() {
		super("Invalid Credentials");
	}
}
