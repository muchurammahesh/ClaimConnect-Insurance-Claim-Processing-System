package com.hospital.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hospital.dto.ApiResponse;
import com.hospital.dto.HospitalDTO;
import com.hospital.dto.HospitalPasswordDTO;
import com.hospital.dto.HospitalUpdateDTO;
import com.hospital.dto.InsuranceCompanyDTO;
import com.hospital.dto.PatientDTO;
import com.hospital.entity.ClaimRequest;
import com.hospital.entity.Hospital;
import com.hospital.exception.InvalidCredentialsException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repo.HospitalRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HospitalServiceImpl implements HospitalService {

	// Logger instance for logging information and errors
	private static final Logger log = LoggerFactory.getLogger(HospitalServiceImpl.class);

	// Repository for performing CRUD operations on Hospital entities
	private HospitalRepository hospitalRepo;

	// Client for interacting with ClaimRequest services
	private ClaimRequestClient crClient;

	private PatientClient pateintClient;

	private InsuranceCompClient icClient;

	private PasswordEncoder passwordEncoder;

	/**
	 * Registers a new hospital in the system.
	 *
	 * @param hospital The hospital entity to be registered.
	 * @return HospitalDTO Data transfer object of the saved hospital.
	 */
	@Override
	public HospitalDTO registerHospital(Hospital hospital) {
		log.info("Registering hospital: {}", hospital.getHospitalName());
		hospital.setHospitalPwd(passwordEncoder.encode(hospital.getHospitalPwd()));
		Hospital savedHospital = hospitalRepo.save(hospital);
		return convertToDTO(savedHospital);
	}

	/**
	 * Retrieves a hospital by their unique ID.
	 *
	 * @param id The unique identifier of the hospital.
	 * @return HospitalDTO Data transfer object of the retrieved hospital.
	 * @throws ResourceNotFoundException If the hospital is not found.
	 */
	@Override
	public HospitalDTO getHospitalById(Long id) {
		log.info("Fetching hospital with id: {}", id);
		Hospital hospital = hospitalRepo.findById(id).orElseThrow(ResourceNotFoundException::new);
		return convertToDTO(hospital);
	}

	/**
	 * Updates an existing hospital's details.
	 *
	 * @param hospital The hospital entity with updated information.
	 * @return HospitalDTO Data transfer object of the updated hospital.
	 * @throws ResourceNotFoundException If the hospital does not exist.
	 */
	@Override
	public HospitalDTO updateHospital(long id, HospitalUpdateDTO hospital) {
		log.info("Updating hospital with id: {}", id);
		Hospital exisitingHospital = hospitalRepo.findById(id).orElse(null);
		if (exisitingHospital == null) {
			throw new ResourceNotFoundException();
		} else {

			if (passwordEncoder.matches(hospital.getHospitalPwd(), exisitingHospital.getHospitalPwd())) {
				exisitingHospital.setHospitalPwd(passwordEncoder.encode(hospital.getHospitalPwd()));
				exisitingHospital.setHospitalName(hospital.getHospitalName());
				Hospital updatedHospital = hospitalRepo.save(exisitingHospital);
				return convertToDTO(updatedHospital);
			} else {
				throw new InvalidCredentialsException();
			}

		}
	}

	/**
	 * Deletes a hospital from the system by their ID.
	 *
	 * @param id The unique identifier of the hospital to be deleted.
	 * @throws ResourceNotFoundException If the hospital does not exist.
	 */
	@Override
	public void deleteHospital(Long id) {
		log.info("Deleting hospital with id: {}", id);
		if (!hospitalRepo.existsById(id)) {
			throw new ResourceNotFoundException();
		}
		hospitalRepo.deleteById(id);
	}

	/**
	 * Authenticates a hospital based on email and password.
	 *
	 * @param email    The hospital's email address.
	 * @param password The hospital's password.
	 * @return HospitalDTO Data transfer object of the authenticated hospital.
	 * @throws ResourceNotFoundException If authentication fails.
	 */
	@Override
	public HospitalDTO loginHospital(String email, String password) {
		Hospital hospital = hospitalRepo.findHospitalByEmail(email);
		if (hospital != null && hospital.getHospitalPwd().equals(password)) {
			log.info("Login hospital successful for email: {}", email);
			return convertToDTO(hospital);
		}
		throw new ResourceNotFoundException("Invalid email or password");
	}

	/**
	 * Generates a new claim request.
	 *
	 * @param cr The ClaimRequest entity to be generated.
	 * @return ClaimRequest The generated claim request.
	 */
	@Override
	public ClaimRequest generateClaimReq(ClaimRequest cr, long hospitalId) {
		log.info("Generating claims for patient id: {}", cr.getPatientId());
		cr.setHospitalId(hospitalId);
		cr.setLastUpdatedBy("hospital");
		cr.setStatus("pending");
		cr.setStatusMessage("Sent for patient approval");
		return crClient.save(cr);
	}

	/**
	 * Updates an existing claim request.
	 *
	 * @param cr The ClaimRequest entity with updated information.
	 * @return ClaimRequest The updated claim request.
	 */
	@Override
	public ClaimRequest updateClaimReq(ClaimRequest cr) {
		log.info("Updating claims with id: {}", cr.getId());
		cr.setStatus("pending");
		cr.setStatusMessage("sent for patient approval");
		cr.setLastUpdatedBy("hospital");
		return crClient.save(cr);
	}

	/**
	 * Retrieves all claim requests associated with a hospital's ID.
	 *
	 * @param hospitalId The unique identifier of the hospital.
	 * @return List of ClaimRequest objects associated with the hospital.
	 * @throws ResourceNotFoundException If the hospital does not exist.
	 */
	@Override
	public List<ClaimRequest> getClaimsByHospitalId(long hospitalId) {
		log.info("Fetching claims by hospital id: {}", hospitalId);
		Hospital existingHospital = hospitalRepo.findById(hospitalId).orElse(null);
		if (existingHospital != null) {
			return crClient.getClaimsByHospitalId(hospitalId);
		}
		throw new ResourceNotFoundException();
	}

	/**
	 * Converts a Hospital entity to a HospitalDTO.
	 *
	 * @param hospital The hospital entity to convert.
	 * @return HospitalDTO The data transfer object representing the hospital.
	 */
	private HospitalDTO convertToDTO(Hospital hospital) {
		return new HospitalDTO(hospital.getHospitalId(), hospital.getHospitalName(), hospital.getHospitalEmail());
	}

	@Override
	public boolean checkEligibility(long patientId) {
//		ApiResponse<PatientDTO> existingPatient  = pateintClient.getPatient(patientId);
//		System.out.println(existingPatient);
//		if(existingPatient.getData() != null) {
//			return true;
//		}
		return false;
	}

	@Override
	public ApiResponse<PatientDTO> getPatientByEmail(String email) {
		System.out.println("inside hospital service imp");
		System.out.println(email);

		return pateintClient.getPatientByEmail(email);

	}

	@Override
	public ApiResponse<PatientDTO> getPatientById(long id) {

		return pateintClient.getPatientById(id);
	}

	@Override
	public ApiResponse<InsuranceCompanyDTO> getIcById(long id) {

		return icClient.getIcById(id);
	}

	@Override
	public HospitalDTO updatePassword(long id, HospitalPasswordDTO hospital) {
		
		log.info("Updating hospital password with id: {}", id);
		Hospital exisitingHospital = hospitalRepo.findById(id).orElse(null);
		if (exisitingHospital == null) {
			throw new ResourceNotFoundException();
		} else {

			if (passwordEncoder.matches(hospital.getOldHospitalPwd(), exisitingHospital.getHospitalPwd())) {
				exisitingHospital.setHospitalPwd(passwordEncoder.encode(hospital.getNewHospitalPwd()));
				Hospital updatedHospital = hospitalRepo.save(exisitingHospital);
				return convertToDTO(updatedHospital);
			} else {
				throw new InvalidCredentialsException();
			}

		}
	}

}
