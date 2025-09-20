package com.insurancecompany.exception;

public class InvalidCredentialsException  extends RuntimeException{
	
	public InvalidCredentialsException() {
		super("Invalid Credentials");
	}
}
