package com.patient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HospitalDTO {
	
	private Long hospitalId;
	private String hospitalName;
	private String hospitalEmail;
}
