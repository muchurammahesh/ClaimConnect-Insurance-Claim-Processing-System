package com.claimrequest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.claimrequest.entity.ClaimRequest;
import com.claimrequest.service.ClaimRequestService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/claims")
@AllArgsConstructor
public class ClaimRequestController {
	
	private ClaimRequestService crSer;
	
	
	@PostMapping("/save")
	public ClaimRequest save(@RequestBody ClaimRequest cr) {
		
		return crSer.save(cr);
	}
	
	@GetMapping("/findById/{id}") 
	public ClaimRequest findById(@PathVariable long id){
		return crSer.findById(id);
	}
	
	@GetMapping("/getClaimsByHospitalId/{hospitalId}")
	public List<ClaimRequest> getClaimsByHospitalId(@PathVariable long hospitalId) {
	
		return crSer.getClaimsByHospitalId(hospitalId);
	}

	@GetMapping("/getClaimsByPatientId/{patientId}")
	public List<ClaimRequest> getClaimsByPatientId(@PathVariable long patientId) {
		return crSer.getClaimsByPatientId(patientId);
		
	}

	@GetMapping("/getClaimsByInsuranceCompId/{insuranceCompId}")
	public List<ClaimRequest> getClaimsByInsuranceCompId(@PathVariable long insuranceCompId) {
		
		return crSer.getClaimsByInsuranceCompId(insuranceCompId);
		
	}
	
	
}
