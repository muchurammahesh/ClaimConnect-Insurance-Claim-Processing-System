package com.hospital.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.hospital.dto.ApiResponse;
import com.hospital.dto.PatientDTO;

@FeignClient(name = "PATIENTSERVICE")
public interface PatientClient {

//	@GetMapping("/api/patient/getPatient")
//	public ApiResponse<PatientDTO> getPatient(@PathVariable Long id);

	@GetMapping("/api/patient/getPatientByEmail")
	public ApiResponse<PatientDTO> getPatientByEmail(@RequestParam String email);

	@GetMapping("/api/patient/getPatientById/{patId}")
	public ApiResponse<PatientDTO> getPatientById(@PathVariable long patId);
}
