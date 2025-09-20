package com.hospital.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hospital.entity.ClaimRequest;

@FeignClient(name = "CLAIMREQUESTSERVICE")
public interface ClaimRequestClient {

	
	@PostMapping("/api/claims/save")
	ClaimRequest save(@RequestBody ClaimRequest cr);
	
	@GetMapping("/api/claims/getClaimsByHospitalId/{hospitalId}")
	List<ClaimRequest> getClaimsByHospitalId(@PathVariable long hospitalId);
}
