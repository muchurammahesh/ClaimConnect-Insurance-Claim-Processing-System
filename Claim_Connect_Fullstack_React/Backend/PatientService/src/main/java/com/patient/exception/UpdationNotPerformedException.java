package com.patient.exception;

public class UpdationNotPerformedException extends RuntimeException {
    public UpdationNotPerformedException() {
        super("Patient insurance association cannot be changed if there are pending claim requests!!");
    }
   
}
