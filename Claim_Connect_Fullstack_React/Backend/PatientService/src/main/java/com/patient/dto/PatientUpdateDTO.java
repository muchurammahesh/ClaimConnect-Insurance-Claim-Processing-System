package com.patient.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientUpdateDTO {
	
	@NotBlank(message = "Patient name is required")
	private String patientName;
	
	@NotBlank(message = "Patient password is required")
    private String patientPassword; // Password for patient login
	
	@NotNull(message = "Insurance company ID is required")
	private long insuranceCompId;
}
