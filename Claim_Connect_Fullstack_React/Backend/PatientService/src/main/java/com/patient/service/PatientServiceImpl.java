package com.patient.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.patient.dto.ApiResponse;
import com.patient.dto.HospitalDTO;
import com.patient.dto.InsuranceCompanyDTO;
import com.patient.dto.PatientDTO;
import com.patient.dto.PatientPasswordDTO;
import com.patient.dto.PatientUpdateDTO;
import com.patient.entity.ClaimRequest;
import com.patient.entity.Patient;
import com.patient.exception.ClaimNotFoundException;
import com.patient.exception.InvalidCredentialsException;
import com.patient.exception.ResourceNotFoundException;
import com.patient.exception.UpdationNotPerformedException;
import com.patient.repo.PatientRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PatientServiceImpl implements PatientService {

	// The type of owner performing claim operations
	private static String ownerType = "patient";

	// Logger instance for logging information and errors
	private static final Logger log = LoggerFactory.getLogger(PatientServiceImpl.class);

	// Repository for performing CRUD operations on Patient entities
	private PatientRepository patRepo;

	// Client for interacting with ClaimRequest services
	private ClaimRequestClient crClient;

	private HospitalClient hosClient;

	private InsuranceCompClient icClient;

	private PasswordEncoder passwordEncoder;

	/**
	 * Registers a new patient in the system.
	 *
	 * @param patient The patient entity to be registered.
	 * @return PatientDTO Data transfer object of the saved patient.
	 */
	@Override
	public PatientDTO registerPatient(Patient patient) {
		log.info("Registering patient: {}", patient.getPatientName());
		patient.setPatientPassword(passwordEncoder.encode(patient.getPatientPassword()));
		Patient savedPatient = patRepo.save(patient);
		return convertToDTO(savedPatient);
	}

	/**
	 * Retrieves a patient by their unique ID.
	 *
	 * @param id The unique identifier of the patient.
	 * @return PatientDTO Data transfer object of the retrieved patient.
	 * @throws ResourceNotFoundException If the patient is not found.
	 */
	@Override
	public PatientDTO getPatientById(Long id) {
		log.info("Fetching patient with id: {}", id);
		Patient patient = patRepo.findById(id).orElseThrow(ResourceNotFoundException::new);
		return convertToDTO(patient);
	}

	/**
	 * Updates an existing patient's details.
	 *
	 * @param updatedPat The patient entity with updated information.
	 * @return PatientDTO Data transfer object of the updated patient.
	 * @throws ResourceNotFoundException If the patient does not exist.
	 */
	@Override
	public PatientDTO updatePatient(long id, PatientUpdateDTO updatedPat) {
		log.info("Updating patient with id: {}", id);
		Patient existingPatient = patRepo.findById(id).orElse(null);
		if (existingPatient == null) {
			throw new ResourceNotFoundException();
		} else {

			if (passwordEncoder.matches(updatedPat.getPatientPassword(), existingPatient.getPatientPassword())) {
				if (existingPatient.getInsuranceCompId() != updatedPat.getInsuranceCompId()) {
					List<ClaimRequest> claims = getClaimsByPatientId(id);
					for (ClaimRequest claim : claims) {
						if (!claim.getStatus().equals("approved")) {
							throw new UpdationNotPerformedException();
						}
					}
				}
				existingPatient.setPatientName(updatedPat.getPatientName());
				existingPatient.setPatientPassword(passwordEncoder.encode(updatedPat.getPatientPassword()));
				existingPatient.setInsuranceCompId(updatedPat.getInsuranceCompId());
				Patient updatedPatient = patRepo.save(existingPatient);
				return convertToDTO(updatedPatient);
			} else {
				throw new InvalidCredentialsException();
			}

		}
	}

	/**
	 * Deletes a patient from the system by their ID.
	 *
	 * @param id The unique identifier of the patient to be deleted.
	 * @throws ResourceNotFoundException If the patient does not exist.
	 */
	@Override
	public void deletePatient(Long id) {
		log.info("Deleting patient with id: {}", id);
		if (!patRepo.existsById(id)) {
			throw new ResourceNotFoundException();
		}
		patRepo.deleteById(id);
	}

	/**
	 * Authenticates a patient based on email and password.
	 *
	 * @param email    The patient's email address.
	 * @param password The patient's password.
	 * @return PatientDTO Data transfer object of the authenticated patient.
	 * @throws ResourceNotFoundException If authentication fails.
	 */
	@Override
	public PatientDTO loginPatient(String email, String password) {
		Patient patient = patRepo.findByPatientEmail(email).orElse(null);
		if (patient != null && patient.getPatientPassword().equals(password)) {
			log.info("Login successful for email: {}", email);
			return convertToDTO(patient);
		}
		throw new ResourceNotFoundException("Invalid email or password");
	}

	/**
	 * Retrieves all claim requests associated with a patient's ID.
	 *
	 * @param patientId The unique identifier of the patient.
	 * @return List of ClaimRequest objects associated with the patient.
	 * @throws ResourceNotFoundException If the patient does not exist.
	 */
	@Override
	public List<ClaimRequest> getClaimsByPatientId(long patientId) {
		// long loggedInUserId = 1; // hardcode
		log.info("Fetching claims by patient id: {}", patientId);
		Patient existingPatient = patRepo.findById(patientId).orElse(null);
		if (existingPatient != null) {
			return crClient.getClaimsByPatientId(patientId);
		}
		throw new ResourceNotFoundException();
	}

	/**
	 * Accepts a claim request by updating its status and forwarding it.
	 *
	 * @param id The unique identifier of the claim request.
	 * @return ClaimRequest The updated claim request.
	 * @throws ClaimNotFoundException If the claim does not exist.
	 */
	@Override
	public ClaimRequest acceptClaim(long id) {
		log.info("Accepting claim with id: {}", id);
		ClaimRequest existingClaim = crClient.findById(id);
		if (existingClaim != null) {
			existingClaim.setLastUpdatedBy(ownerType);
			existingClaim.setStatus("accepted");
			existingClaim.setStatusMessage("Forwarded to Insurance Provider");
			return crClient.save(existingClaim);
		}
		throw new ClaimNotFoundException();
	}

	/**
	 * Reverts a claim request with a provided status message.
	 *
	 * @param id            The unique identifier of the claim request.
	 * @param statusMessage The message explaining the reason for revert.
	 * @return ClaimRequest The updated claim request.
	 * @throws ClaimNotFoundException If the claim does not exist.
	 */
	@Override
	public ClaimRequest revertClaim(long id, String statusMessage) {
		log.info("Reverting claim with id: {}", id);
		ClaimRequest existingClaim = crClient.findById(id);
		if (existingClaim != null) {
			existingClaim.setLastUpdatedBy(ownerType);
			existingClaim.setStatus("reverted");
			existingClaim.setStatusMessage(statusMessage);
			return crClient.save(existingClaim);
		}
		throw new ClaimNotFoundException();
	}

	/**
	 * Rejects a claim request with a provided status message.
	 *
	 * @param id            The unique identifier of the claim request.
	 * @param statusMessage The message explaining the reason for rejection.
	 * @return ClaimRequest The updated claim request.
	 * @throws ClaimNotFoundException If the claim does not exist.
	 */
	@Override
	public ClaimRequest rejectClaim(long id, String statusMessage) {
		log.info("Rejecting claim with id: {}", id);
		ClaimRequest existingClaim = crClient.findById(id);
		if (existingClaim != null) {
			existingClaim.setLastUpdatedBy(ownerType);
			existingClaim.setStatus("rejected");
			existingClaim.setStatusMessage(statusMessage);
			return crClient.save(existingClaim);
		}
		throw new ClaimNotFoundException();
	}

	/**
	 * Converts a Patient entity to a PatientDTO.
	 *
	 * @param patient The patient entity to convert.
	 * @return PatientDTO The data transfer object representing the patient.
	 */
	private PatientDTO convertToDTO(Patient patient) {
		return new PatientDTO(patient.getPatientId(), patient.getPatientName(), patient.getPatientEmail(),
				patient.getInsuranceCompId());
	}

	@Override
	public PatientDTO getPatientByEmail(String email) {
		Patient patient = patRepo.findByPatientEmail(email).orElse(null);
		if (patient != null) {
			log.info("Pateint found for email: {}", email);
			return convertToDTO(patient);
		}
		throw new ResourceNotFoundException("Invalid email or password");
	}

	@Override
	public ApiResponse<HospitalDTO> getHospitalById(long id) {

		return hosClient.getHospital(id);
	}

	@Override
	public ApiResponse<InsuranceCompanyDTO> getIcById(long id) {

		return icClient.getIcById(id);
	}

	@Override
	public PatientDTO updatePassword(long id, PatientPasswordDTO patient) {

		log.info("Updating patient with id: {}", id);
		Patient existingPatient = patRepo.findById(id).orElse(null);
		if (existingPatient == null) {
			throw new ResourceNotFoundException();
		} else {

			if (passwordEncoder.matches(patient.getOldPatientPassword(), existingPatient.getPatientPassword())) {
//				List<ClaimRequest> claims = getClaimsByPatientId(id);
//				for(ClaimRequest claim : claims) {
//					if(!claim.getStatus().equals("approved")) {
//						throw new UpdationNotPerformedException();
//					}
//				}
				existingPatient.setPatientPassword(passwordEncoder.encode(patient.getNewPatientPassword()));
				Patient updatedPatient = patRepo.save(existingPatient);
				return convertToDTO(updatedPatient);
			} else {
				throw new InvalidCredentialsException();
			}

		}
	}

}
