package com.patient.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId; // Unique identifier for each patient

    @NotBlank(message = "Patient name is required")
    private String patientName; // Full name of the patient

    @NotBlank(message = "Patient email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String patientEmail; // Email address of the patient

    @NotBlank(message = "Patient password is required")
    private String patientPassword; // Password for patient login

    @NotNull(message = "Insurance company ID is required")
    private Long insuranceCompId; // Associated insurance company ID
    
}
