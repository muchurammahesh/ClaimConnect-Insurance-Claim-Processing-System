package com.hospital.exception;

public class InvalidCredentialsException  extends RuntimeException{
	
	public InvalidCredentialsException() {
		super("Invalid Credentials");
	}
}
