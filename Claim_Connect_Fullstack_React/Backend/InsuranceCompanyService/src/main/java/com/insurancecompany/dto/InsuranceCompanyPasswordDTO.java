package com.insurancecompany.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceCompanyPasswordDTO {
    @NotBlank(message = "Insurance company old password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String oldInsuranceCompPwd; // Password for insurance company login
    
    @NotBlank(message = "Insurance company new password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String newInsuranceCompPwd; // Password for insurance company login
}
