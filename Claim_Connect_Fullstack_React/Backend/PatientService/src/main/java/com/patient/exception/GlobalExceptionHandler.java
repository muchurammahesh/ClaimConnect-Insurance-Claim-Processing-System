package com.patient.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.patient.dto.ApiResponse;
import com.patient.dto.InsuranceCompanyDTO;
import com.patient.dto.PatientDTO;
import com.patient.entity.ClaimRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	// Handle all uncaught exceptions
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleAllExceptions(Exception ex) {
		log.error("Exception: ", ex);
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
//	@ResponseStatus(HttpStatus.CONFLICT)
	public ApiResponse<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
		log.error("Data integrity violation: ", ex);
		return new ApiResponse<>(HttpStatus.CONFLICT, "Patient with this email already exists", null);
	}

	// Handle Patient Not Found Exception
	@ExceptionHandler(ResourceNotFoundException.class)
	public ApiResponse<PatientDTO> handleResourceNotFoundException(ResourceNotFoundException ex) {
		log.error("ResourceNotFoundException: ", ex);
		return new ApiResponse<>(HttpStatus.NOT_FOUND, ex.getMessage(), null);

	}

	// Handle Patient Not Found Exception
	@ExceptionHandler(UpdationNotPerformedException.class)
	public ApiResponse<PatientDTO> handleUpdationNotPerformedException(UpdationNotPerformedException ex) {
		log.error("UpdationNotPerformedException: ", ex);
		return new ApiResponse<>(HttpStatus.CONFLICT, ex.getMessage(), null);

	}

	// Handle insurance company Not Found Exception
	@ExceptionHandler(InvalidCredentialsException.class)
	public ApiResponse<PatientDTO> handleInvalidCredentialsException(InvalidCredentialsException ex) {
		log.error("ResourceNotFoundException: ", ex);

		return new ApiResponse<>(HttpStatus.UNAUTHORIZED, ex.getMessage(), null);

	}

	// Handle Claim Not Found Exception
	@ExceptionHandler(ClaimNotFoundException.class)
	public ApiResponse<ClaimRequest> handleClaimNotFoundException(ClaimNotFoundException ex) {
		log.error("ClaimNotFoundException: ", ex);
		return new ApiResponse<>(HttpStatus.NOT_FOUND, ex.getMessage(), null);

	}

	// Handle Validation Errors
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
		log.error("ValidationException: ", ex);
		StringBuilder errors = new StringBuilder();
		ex.getBindingResult().getFieldErrors().forEach(
				error -> errors.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; "));
		return new ResponseEntity<>("Validation error(s): " + errors.toString(), HttpStatus.BAD_REQUEST);
	}

}
