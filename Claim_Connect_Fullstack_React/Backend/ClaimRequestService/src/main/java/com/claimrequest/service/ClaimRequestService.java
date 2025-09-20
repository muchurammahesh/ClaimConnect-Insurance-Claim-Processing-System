package com.claimrequest.service;

import java.util.List;

import com.claimrequest.entity.ClaimRequest;

public interface ClaimRequestService {
	
	List<ClaimRequest> getClaimsByHospitalId(long hospitalId);
	
	List<ClaimRequest> getClaimsByPatientId(long patientId);
	
	List<ClaimRequest> getClaimsByInsuranceCompId(long insuranceCompId);

	ClaimRequest save(ClaimRequest cr);

	ClaimRequest findById(long id);
}
