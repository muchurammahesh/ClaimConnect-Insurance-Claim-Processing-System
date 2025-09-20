package com.hospital.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HospitalUpdateDTO {
	
    @NotBlank(message = "Hospital name is required")
    private String hospitalName; // Name of the hospital

    @NotBlank(message = "Hospital password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String hospitalPwd; // Password for hospital login
}
