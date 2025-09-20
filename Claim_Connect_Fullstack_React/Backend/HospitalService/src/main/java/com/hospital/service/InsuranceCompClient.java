package com.hospital.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hospital.dto.ApiResponse;
import com.hospital.dto.InsuranceCompanyDTO;

@FeignClient(name = "INSURANCECOMPANYSERVICE")
public interface InsuranceCompClient {

	@GetMapping("/api/insuranceComp/getIcById/{id}")
	public ApiResponse<InsuranceCompanyDTO> getIcById(@PathVariable long id);
}
