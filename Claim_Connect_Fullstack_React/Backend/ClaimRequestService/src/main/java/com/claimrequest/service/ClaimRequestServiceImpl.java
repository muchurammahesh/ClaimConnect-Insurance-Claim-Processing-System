package com.claimrequest.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.claimrequest.entity.ClaimRequest;
import com.claimrequest.exception.ResourceNotFoundException;
import com.claimrequest.repo.ClaimRequestRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ClaimRequestServiceImpl implements ClaimRequestService {

	private static final Logger log = LoggerFactory.getLogger(ClaimRequestServiceImpl.class);
	
	private ClaimRequestRepository crRepo;
	
	@Override
	public List<ClaimRequest> getClaimsByHospitalId(long hospitalId) {
		log.info("Fetching claims by hospital id: {}", hospitalId);
		return crRepo.findByHospitalId(hospitalId);
	}

	@Override
	public List<ClaimRequest> getClaimsByPatientId(long patientId) {
		log.info("Fetching claims by patient id: {}", patientId);
		return crRepo.findByPatientId(patientId);
	}

	@Override
	public List<ClaimRequest> getClaimsByInsuranceCompId(long insuranceCompId) {
		log.info("Fetching claims by insurance id: {}", insuranceCompId);
		return crRepo.getByInsuranceCompId(insuranceCompId);
	}

	@Override
	public ClaimRequest save(ClaimRequest cr) {
		log.info("Saving claims by for patient id: {}", cr.getPatientId());
		return crRepo.save(cr);
	}

	@Override
	public ClaimRequest findById(long id) {
		log.info("Fetching claims by id: {}", id);
		return crRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClaimRequest not found with id: " + id));
	}

}
