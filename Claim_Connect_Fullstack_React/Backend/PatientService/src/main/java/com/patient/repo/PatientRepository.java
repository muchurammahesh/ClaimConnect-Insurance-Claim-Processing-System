package com.patient.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.patient.entity.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
	
//	@Query("select p from Patient p where p.patientEmail = ?1")
//	Patient findByPatientEmail(String email);
	
	Optional<Patient> findByPatientEmail(String email);
}
