package com.patient.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.patient.dto.ApiResponse;
import com.patient.dto.InsuranceCompanyDTO;

@FeignClient(name = "INSURANCECOMPANYSERVICE")
public interface InsuranceCompClient {
	
	@GetMapping("/api/insuranceComp/getIcById/{id}")
	public ApiResponse<InsuranceCompanyDTO> getIcById(@PathVariable long id);
}