package com.hospital.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hospital.entity.Hospital;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long>{
	
	@Query("select h from Hospital h where h.hospitalEmail = ?1")
	Hospital findHospitalByEmail(String email);
	
	Optional<Hospital> findByHospitalEmail(String email);

}