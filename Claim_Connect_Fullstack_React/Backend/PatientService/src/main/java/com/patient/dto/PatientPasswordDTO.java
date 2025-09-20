package com.patient.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientPasswordDTO {

	@NotBlank(message = "Patient old password is required")
    private String oldPatientPassword; // Password for patient login
	
	@NotBlank(message = "Patient new password is required")
    private String newPatientPassword; // Password for patient login
}
