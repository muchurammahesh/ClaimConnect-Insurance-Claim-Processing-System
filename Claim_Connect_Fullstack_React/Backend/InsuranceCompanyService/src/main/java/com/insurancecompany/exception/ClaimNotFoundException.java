package com.insurancecompany.exception;

public class ClaimNotFoundException extends RuntimeException {

	public ClaimNotFoundException() {
		super("Claim with given claim id not found!");
	}
}

