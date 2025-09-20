package com.patient.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.patient.dto.ApiResponse;
import com.patient.dto.HospitalDTO;

@FeignClient(name = "HOSPITALSERVICE")
public interface HospitalClient {
	
	@GetMapping("/api/hospital/getHospitalById/{id}")
    public ApiResponse<HospitalDTO> getHospital(@PathVariable long id);
}