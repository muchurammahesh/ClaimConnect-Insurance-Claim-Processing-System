package com.patient.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.patient.entity.ClaimRequest;


@FeignClient(name = "CLAIMREQUESTSERVICE")
public interface ClaimRequestClient {
	
	
	@PostMapping("/api/claims/save")
	ClaimRequest save(@RequestBody ClaimRequest cr);
	
	@GetMapping("/api/claims/findById/{id}") 
	ClaimRequest findById(@PathVariable long id);
	
	@GetMapping("/api/claims/getClaimsByPatientId/{patientId}")
	List<ClaimRequest> getClaimsByPatientId(@PathVariable long patientId);
}