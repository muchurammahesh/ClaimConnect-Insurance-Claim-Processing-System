package com.insurancecompany.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceCompanyUpdateDTO {
	@NotBlank(message = "Insurance company name is required")
	private String insuranceCompName;
	@NotBlank(message = "Insurance company password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
	private String insuranceCompPwd; // Password for insurance company login
}
