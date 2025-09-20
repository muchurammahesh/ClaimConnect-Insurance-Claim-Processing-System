package com.patient.service;

import java.util.List;

import com.patient.dto.ApiResponse;
import com.patient.dto.HospitalDTO;
import com.patient.dto.InsuranceCompanyDTO;
import com.patient.dto.PatientDTO;
import com.patient.dto.PatientPasswordDTO;
import com.patient.dto.PatientUpdateDTO;
import com.patient.entity.ClaimRequest;
import com.patient.entity.Patient;

public interface PatientService {

	public PatientDTO registerPatient(Patient patient);

	public PatientDTO getPatientById(Long id);

	public PatientDTO updatePatient(long id, PatientUpdateDTO updatedPat);

	public void deletePatient(Long id);

	public PatientDTO loginPatient(String email, String password);

	public List<ClaimRequest> getClaimsByPatientId(long patientId);
	
	public ClaimRequest acceptClaim(long id);

	public ClaimRequest revertClaim(long id, String statusMessage);
	
	public ClaimRequest rejectClaim(long id, String statusMessage);

	public PatientDTO getPatientByEmail(String email);

	public ApiResponse<HospitalDTO> getHospitalById(long id);

	public ApiResponse<InsuranceCompanyDTO> getIcById(long id);

	public PatientDTO updatePassword(long id, PatientPasswordDTO patient);

}
