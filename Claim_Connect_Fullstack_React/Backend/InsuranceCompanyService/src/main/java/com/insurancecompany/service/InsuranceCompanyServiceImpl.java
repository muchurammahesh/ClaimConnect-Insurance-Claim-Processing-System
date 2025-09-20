package com.insurancecompany.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.insurancecompany.dto.ApiResponse;
import com.insurancecompany.dto.HospitalDTO;
import com.insurancecompany.dto.InsuranceCompanyDTO;
import com.insurancecompany.dto.InsuranceCompanyPasswordDTO;
import com.insurancecompany.dto.InsuranceCompanyUpdateDTO;
import com.insurancecompany.dto.PatientDTO;
import com.insurancecompany.entity.ClaimRequest;
import com.insurancecompany.entity.InsuranceCompany;
import com.insurancecompany.exception.ClaimNotFoundException;
import com.insurancecompany.exception.InvalidCredentialsException;
import com.insurancecompany.exception.ResourceNotFoundException;
import com.insurancecompany.repo.InsuranceCompanyRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InsuranceCompanyServiceImpl implements InsuranceCompanyService {

    // The type of owner performing claim operations
    private static String ownerType = "insuranceComp";

    // Logger instance for logging information and errors
    private static final Logger log = LoggerFactory.getLogger(InsuranceCompanyServiceImpl.class);

    // Repository for performing CRUD operations on InsuranceCompany entities
    private InsuranceCompanyRepository icRepo;

    // Client for interacting with ClaimRequest services
    private ClaimRequestClient crClient;
    
    private HospitalClient hospitalClient;
    
    private PatientClient patientClient;
    
    private PasswordEncoder passwordEncoder;

    /**
     * Registers a new insurance company in the system.
     *
     * @param insuranceCompany The insurance company entity to be registered.
     * @return InsuranceCompanyDTO Data transfer object of the saved insurance company.
     */
    @Override
    public InsuranceCompanyDTO registerInsuranceCompany(InsuranceCompany insuranceCompany) {
        log.info("Registering insurance company: {}", insuranceCompany.getInsuranceCompName());
        insuranceCompany.setInsuranceCompPwd(passwordEncoder.encode(insuranceCompany.getInsuranceCompPwd()));
        InsuranceCompany savedCompany = icRepo.save(insuranceCompany);
        return convertToDTO(savedCompany);
    }

    /**
     * Retrieves an insurance company by their unique ID.
     *
     * @param id The unique identifier of the insurance company.
     * @return InsuranceCompanyDTO Data transfer object of the retrieved insurance company.
     * @throws ResourceNotFoundException If the insurance company is not found.
     */
    @Override
    public InsuranceCompanyDTO getInsuranceCompanyById(Long id) {
        log.info("Fetching insurance company with id: {}", id);
        InsuranceCompany company = icRepo.findById(id).orElseThrow(ResourceNotFoundException::new);
        return convertToDTO(company);
    }

    /**
     * Updates an existing insurance company's details.
     *
     * @param insuranceCompany The insurance company entity with updated information.
     * @return InsuranceCompanyDTO Data transfer object of the updated insurance company.
     * @throws ResourceNotFoundException If the insurance company does not exist.
     */
    @Override
    public InsuranceCompanyDTO updateInsuranceCompany(long id, InsuranceCompanyUpdateDTO insuranceCompany) {
        log.info("Updating insurance company with id: {}", id);
        InsuranceCompany existingRecord = icRepo.findById(id).orElse(null);
        if (existingRecord == null) {
            throw new ResourceNotFoundException();
        }
        if(passwordEncoder.matches(insuranceCompany.getInsuranceCompPwd(), existingRecord.getInsuranceCompPwd())) {
        	existingRecord.setInsuranceCompName(insuranceCompany.getInsuranceCompName());
        	InsuranceCompany savedCompany = icRepo.save(existingRecord);
        	return convertToDTO(savedCompany);
        }
        else {
        	throw new InvalidCredentialsException();
        }
    }

    /**
     * Deletes an insurance company from the system by their ID.
     *
     * @param id The unique identifier of the insurance company to be deleted.
     * @throws ResourceNotFoundException If the insurance company does not exist.
     */
    @Override
    public void deleteInsuranceCompany(Long id) {
        log.info("Deleting insurance company with id: {}", id);
        if (!icRepo.existsById(id)) {
            throw new ResourceNotFoundException();
        }
        icRepo.deleteById(id);
    }

    /**
     * Authenticates an insurance company based on email and password.
     *
     * @param email    The insurance company's email address.
     * @param password The insurance company's password.
     * @return InsuranceCompanyDTO Data transfer object of the authenticated insurance company.
     * @throws ResourceNotFoundException If authentication fails.
     */
    @Override
    public InsuranceCompanyDTO loginInsuranceCompany(String email, String password) {
        InsuranceCompany existingRecord = icRepo.findInsuranceCompanyByEmail(email);
        if (existingRecord != null && existingRecord.getInsuranceCompPwd().equals(password)) {
            log.info("Login successful for insurance company with email: {}", email);
            return convertToDTO(existingRecord);
        }
        throw new ResourceNotFoundException("Invalid email or password");
    }

    /**
     * Retrieves all claim requests associated with an insurance company's ID.
     *
     * @param id The unique identifier of the insurance company.
     * @return List of ClaimRequest objects associated with the insurance company.
     * @throws ResourceNotFoundException If the insurance company does not exist.
     */
    @Override
    public List<ClaimRequest> getClaimsByInsuranceCompId(long id) {
        InsuranceCompany existingInsuranceComp = icRepo.findById(id).orElse(null);
        if (existingInsuranceComp != null) {
            return crClient.getClaimsByInsuranceCompId(id);
        }
        throw new ResourceNotFoundException();
    }

    /**
     * Approves a claim request by updating its status and forwarding it.
     *
     * @param id The unique identifier of the claim request.
     * @return ClaimRequest The updated claim request.
     * @throws ClaimNotFoundException If the claim does not exist.
     */
    @Override
    public ClaimRequest approveClaim(long id) {
        log.info("Approving claim with id: {}", id);
        ClaimRequest existingClaim = crClient.findById(id);
        if (existingClaim != null) {
            existingClaim.setLastUpdatedBy(ownerType);
            existingClaim.setStatus("approved");
            existingClaim.setStatusMessage("Claim is approved by insurance company");
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
     * Converts an InsuranceCompany entity to an InsuranceCompanyDTO.
     *
     * @param company The insurance company entity to convert.
     * @return InsuranceCompanyDTO The data transfer object representing the insurance company.
     */
    private InsuranceCompanyDTO convertToDTO(InsuranceCompany company) {
        return new InsuranceCompanyDTO(
            company.getInsuranceCompId(),
            company.getInsuranceCompName(),
            company.getInsuranceCompEmail()
        );
    }

	@Override
	public List<InsuranceCompanyDTO> getAllIcs() {
		log.info("Fetching insurance companies");
        List<InsuranceCompany> companies = icRepo.findAll();
        System.out.println(companies.size());
//        		orElseThrow(ResourceNotFoundException::new);
        List<InsuranceCompanyDTO> returnedCompanies = new ArrayList<>();
        for(InsuranceCompany d : companies) {
        	System.out.println("inside for");
        	returnedCompanies.add(convertToDTO(d));
        }
		return returnedCompanies;
	}

	@Override
	public ApiResponse<HospitalDTO> getHospitalById(long id) {
		
		return hospitalClient.getHospital(id);
	}

	@Override
	public ApiResponse<PatientDTO> getPatientById(long id) {
		
		return patientClient.getPatientById(id);
	}

	@Override
	public InsuranceCompanyDTO updatePassword(long id, InsuranceCompanyPasswordDTO insuranceCompany) {
		
		log.info("Updating insurance company with id: {}", id);
        InsuranceCompany existingRecord = icRepo.findById(id).orElse(null);
        if (existingRecord == null) {
            throw new ResourceNotFoundException();
        }
        if(passwordEncoder.matches(insuranceCompany.getOldInsuranceCompPwd(), existingRecord.getInsuranceCompPwd())) {
        	existingRecord.setInsuranceCompPwd(passwordEncoder.encode(insuranceCompany.getNewInsuranceCompPwd()));
        	InsuranceCompany savedCompany = icRepo.save(existingRecord);
        	return convertToDTO(savedCompany);
        }
        else {
        	throw new InvalidCredentialsException();
        }
	}

}
