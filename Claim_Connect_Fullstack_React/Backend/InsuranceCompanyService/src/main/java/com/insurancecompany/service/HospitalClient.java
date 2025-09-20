package com.insurancecompany.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.insurancecompany.dto.ApiResponse;
import com.insurancecompany.dto.HospitalDTO;

@FeignClient(name = "HOSPITALSERVICE")
public interface HospitalClient {
	
	@GetMapping("/api/hospital/getHospitalById/{id}")
    public ApiResponse<HospitalDTO> getHospital(@PathVariable long id);
}