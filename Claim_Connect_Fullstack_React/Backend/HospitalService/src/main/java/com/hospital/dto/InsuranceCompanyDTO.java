package com.hospital.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceCompanyDTO {
	private Long insuranceCompId;
	private String insuranceCompName;
	private String insuranceCompEmail;
}
