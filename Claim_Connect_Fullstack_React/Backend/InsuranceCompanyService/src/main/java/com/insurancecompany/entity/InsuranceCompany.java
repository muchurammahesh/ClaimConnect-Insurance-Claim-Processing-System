package com.insurancecompany.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long insuranceCompId; // Unique identifier for the insurance company

    @NotBlank(message = "Insurance company name is required")
    private String insuranceCompName; // Name of the insurance company

    @Column(unique = true)
    @NotBlank(message = "Insurance company email is required")
    @Email(message = "Invalid email format")
    private String insuranceCompEmail; // Email address of the insurance company

    @NotBlank(message = "Insurance company password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String insuranceCompPwd; // Password for insurance company login
}
