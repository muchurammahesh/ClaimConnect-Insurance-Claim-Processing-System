package com.insurancecompany.service;

import java.util.List;

import com.insurancecompany.dto.ApiResponse;
import com.insurancecompany.dto.HospitalDTO;
import com.insurancecompany.dto.InsuranceCompanyDTO;
import com.insurancecompany.dto.InsuranceCompanyPasswordDTO;
import com.insurancecompany.dto.InsuranceCompanyUpdateDTO;
import com.insurancecompany.dto.PatientDTO;
import com.insurancecompany.entity.ClaimRequest;
import com.insurancecompany.entity.InsuranceCompany;

public interface InsuranceCompanyService {
	
	public InsuranceCompanyDTO	 registerInsuranceCompany(InsuranceCompany insuranceComapny);

	public InsuranceCompanyDTO getInsuranceCompanyById(Long id);

	public InsuranceCompanyDTO updateInsuranceCompany(long id, InsuranceCompanyUpdateDTO insuranceComapny);

	public void deleteInsuranceCompany(Long id);

	public InsuranceCompanyDTO loginInsuranceCompany(String email, String password);
	
	public List<ClaimRequest> getClaimsByInsuranceCompId(long id);
	
	public ClaimRequest approveClaim(long id);
	
	public ClaimRequest revertClaim(long id, String statusMessage);
	
	public ClaimRequest rejectClaim(long id, String statusMessage);

	public List<InsuranceCompanyDTO> getAllIcs();

	public ApiResponse<HospitalDTO> getHospitalById(long id);

	public ApiResponse<PatientDTO> getPatientById(long id);

	public InsuranceCompanyDTO updatePassword(long id, InsuranceCompanyPasswordDTO insuranceCompany);
	
	
}
