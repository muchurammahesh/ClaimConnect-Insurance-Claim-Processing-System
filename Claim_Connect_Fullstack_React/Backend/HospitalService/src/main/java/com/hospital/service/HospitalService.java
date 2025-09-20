package com.hospital.service;

import java.util.List;

import com.hospital.dto.ApiResponse;
import com.hospital.dto.HospitalDTO;
import com.hospital.dto.HospitalPasswordDTO;
import com.hospital.dto.HospitalUpdateDTO;
import com.hospital.dto.InsuranceCompanyDTO;
import com.hospital.dto.PatientDTO;
import com.hospital.entity.ClaimRequest;
import com.hospital.entity.Hospital;

import jakarta.validation.Valid;

public interface HospitalService {

	public HospitalDTO registerHospital(Hospital hospital);
	
	public HospitalDTO getHospitalById(Long id);
	
	public HospitalDTO updateHospital(long id, HospitalUpdateDTO hospital);
	
	public void deleteHospital(Long id);
	
	public HospitalDTO loginHospital(String email, String password);
	
	public ClaimRequest generateClaimReq(ClaimRequest cr, long hospitalId);

	public ClaimRequest updateClaimReq(ClaimRequest cr);
	
	public List<ClaimRequest> getClaimsByHospitalId(long hospitalId);

	public boolean checkEligibility(long patientId);

	ApiResponse<PatientDTO> getPatientByEmail(String email);

	public ApiResponse<PatientDTO> getPatientById(long id);

	public ApiResponse<InsuranceCompanyDTO> getIcById(long id);

	public HospitalDTO updatePassword(long id, HospitalPasswordDTO hospital);
}
