package com.claimrequest.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.claimrequest.entity.ClaimRequest;

@Repository
public interface ClaimRequestRepository extends JpaRepository<ClaimRequest, Long> {

	
	List<ClaimRequest> findByHospitalId(long hospitalId);
	
	@Query("select c from ClaimRequest c where c.patientId = ?1")
	List<ClaimRequest> findByPatientId(long patientId);
	
	@Query("select c from ClaimRequest c where c.insuranceCompId = ?1")
	List<ClaimRequest> getByInsuranceCompId(long insuranceCompId);
}
