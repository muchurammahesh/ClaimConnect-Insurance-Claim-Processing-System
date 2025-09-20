package com.insurancecompany.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.insurancecompany.entity.InsuranceCompany;

@Repository
public interface InsuranceCompanyRepository extends JpaRepository<InsuranceCompany, Long> {

	@Query("select ic from InsuranceCompany ic where ic.insuranceCompEmail = ?1")
	InsuranceCompany findInsuranceCompanyByEmail(String email);
	
	Optional<InsuranceCompany> findByInsuranceCompEmail(String email);

}
