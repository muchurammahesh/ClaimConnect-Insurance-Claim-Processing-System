package com.insurancecompany.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insurancecompany.dto.ApiResponse;
import com.insurancecompany.dto.HospitalDTO;
import com.insurancecompany.dto.InsuranceCompanyDTO;
import com.insurancecompany.dto.InsuranceCompanyPasswordDTO;
import com.insurancecompany.dto.InsuranceCompanyUpdateDTO;
import com.insurancecompany.dto.PatientDTO;
import com.insurancecompany.entity.ClaimRequest;
import com.insurancecompany.entity.InsuranceCompany;
import com.insurancecompany.repo.InsuranceCompanyRepository;
import com.insurancecompany.service.InsuranceCompanyService;
import com.insurancecompany.utils.JwtUtils;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/insuranceComp")
@AllArgsConstructor
public class InsuranceCompanyController {

	// Service layer dependency for handling insurance company-related operations
	private InsuranceCompanyService icSer;

	private AuthenticationManager authManager;

	private InsuranceCompanyRepository icRepo;

	private JwtUtils jwtUtils;

	/**
	 * Registers a new insurance company.
	 *
	 * @param insuranceCompany The insurance company entity to be registered.
	 * @return ApiResponse containing the registered InsuranceCompanyDTO and a
	 *         success message.
	 */
	@PostMapping("/signup")
	public ApiResponse<InsuranceCompanyDTO> registerPatient(@RequestBody InsuranceCompany insuranceCompany) {
		InsuranceCompanyDTO registeredCompany = icSer.registerInsuranceCompany(insuranceCompany);
		return new ApiResponse<>(HttpStatus.CREATED, "Insurance company registered successfully", registeredCompany);
	}

	@PostMapping("/authenticate")
	public ApiResponse<String> authenticateAndGetToken(@RequestBody InsuranceCompany insuranceCompany) {

		Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(
				insuranceCompany.getInsuranceCompEmail(), insuranceCompany.getInsuranceCompPwd()));

		if (authentication.isAuthenticated()) {

			InsuranceCompany foundUser = icRepo.findByInsuranceCompEmail(insuranceCompany.getInsuranceCompEmail())
					.orElse(null);

			String token = jwtUtils.generateToken(foundUser.getInsuranceCompEmail(), foundUser.getInsuranceCompId(),
					"Insurance");
			
			return new ApiResponse<>(HttpStatus.FOUND, "Insurance Company signed-in successfully", token);

		} else {
			throw new UsernameNotFoundException("Invalid User Request!!");
		}
	}

	/**
	 * Retrieves an insurance company by their ID.
	 *
	 * @param id The unique identifier of the insurance company.
	 * @return ApiResponse containing the InsuranceCompanyDTO and a success message.
	 */
	@GetMapping("/getIc")
	public ApiResponse<InsuranceCompanyDTO> getInsuranceCompany(@RequestHeader String userId) {
		long id = Long.parseLong(userId);
		InsuranceCompanyDTO company = icSer.getInsuranceCompanyById(id);
		return new ApiResponse<>(HttpStatus.OK, "Insurance company retrieved successfully", company);
	}
	
	
	@GetMapping("/getIcById/{id}")
	public ApiResponse<InsuranceCompanyDTO> getIcById(@PathVariable long id) {
		InsuranceCompanyDTO company = icSer.getInsuranceCompanyById(id);
		return new ApiResponse<>(HttpStatus.OK, "Insurance company retrieved successfully", company);
	}

	/**
	 * Updates an existing insurance company's information.
	 *
	 * @param insuranceCompany The insurance company entity with updated
	 *                         information.
	 * @return ApiResponse containing the updated InsuranceCompanyDTO and a success
	 *         message.
	 */
	@PutMapping("/update")
	public ApiResponse<InsuranceCompanyDTO> updateInsuranceCompany(@RequestHeader("userId") String userId, @RequestBody InsuranceCompanyUpdateDTO insuranceCompany) {
		long id = Long.parseLong(userId);
		InsuranceCompanyDTO updatedCompany = icSer.updateInsuranceCompany(id, insuranceCompany);
		return new ApiResponse<>(HttpStatus.OK, "Insurance company updated successfully", updatedCompany);
	}
	
	@PutMapping("/updatePassword")
	public ApiResponse<InsuranceCompanyDTO> updatePassword(@RequestHeader("userId") String userId, @RequestBody InsuranceCompanyPasswordDTO insuranceCompany) {
		long id = Long.parseLong(userId);
		InsuranceCompanyDTO updatedCompany = icSer.updatePassword(id, insuranceCompany);
		return new ApiResponse<>(HttpStatus.OK, "Insurance company password updated successfully", updatedCompany);
	}

	/**
	 * Deletes an insurance company by their ID.
	 *
	 * @param id The unique identifier of the insurance company to be deleted.
	 * @return ApiResponse containing a success message.
	 */
	@DeleteMapping("/{id}")
	public ApiResponse<String> deleteInsuranceCompany(@PathVariable Long id) {
		icSer.deleteInsuranceCompany(id);
		return new ApiResponse<>(HttpStatus.OK, "Insurance company deleted successfully", "Success");
	}

	/**
	 * Authenticates an insurance company using email and password.
	 *
	 * @param email    The insurance company's email address.
	 * @param password The insurance company's password.
	 * @return ApiResponse containing the InsuranceCompanyDTO and a success message.
	 */
	@PostMapping("/login")
	public ApiResponse<InsuranceCompanyDTO> loginInsuranceCompany(@RequestParam String email,
			@RequestParam String password) {
		InsuranceCompanyDTO company = icSer.loginInsuranceCompany(email, password);
		return new ApiResponse<>(HttpStatus.OK, "Login Success", company);
	}

	/**
	 * Retrieves all claims associated with an insurance company by their ID.
	 *
	 * @param id The unique identifier of the insurance company.
	 * @return ApiResponse containing the list of ClaimRequest objects and a success
	 *         message.
	 */
	@GetMapping("/getClaimByICId")
	public ApiResponse<List<ClaimRequest>> getClaimsByInsuranceCompId(@RequestHeader String userId) {
		long id = Long.parseLong(userId);
		List<ClaimRequest> claims = icSer.getClaimsByInsuranceCompId(id);
		return new ApiResponse<>(HttpStatus.OK, "Claims retrieved successfully", claims);
	}

	/**
	 * Approves a claim request by its ID.
	 *
	 * @param id The unique identifier of the claim.
	 * @return ApiResponse containing the updated ClaimRequest and a success
	 *         message.
	 */
	@PutMapping("/approveClaim/{id}")
	public ApiResponse<ClaimRequest> approveClaim(@PathVariable long id) {
		ClaimRequest claim = icSer.approveClaim(id);
		return new ApiResponse<>(HttpStatus.OK, "Claim approved successfully", claim);
	}

	/**
	 * Reverts a claim request by its ID with a provided status message.
	 *
	 * @param id            The unique identifier of the claim.
	 * @param statusMessage The message explaining the reason for revert.
	 * @return ApiResponse containing the updated ClaimRequest and a success
	 *         message.
	 */
	@PutMapping("/revertClaim/{id}")
	public ApiResponse<ClaimRequest> revertClaim(@PathVariable long id, @RequestBody String statusMessage) {
		ClaimRequest claim = icSer.revertClaim(id, statusMessage);
		return new ApiResponse<>(HttpStatus.OK, "Claim reverted successfully", claim);
	}

	/**
	 * Rejects a claim request by its ID with a provided status message.
	 *
	 * @param id            The unique identifier of the claim.
	 * @param statusMessage The message explaining the reason for rejection.
	 * @return ApiResponse containing the updated ClaimRequest and a success
	 *         message.
	 */
	@PutMapping("/rejectClaim/{id}")
	public ApiResponse<ClaimRequest> rejectClaim(@PathVariable long id, @RequestBody String statusMessage) {
		ClaimRequest claim = icSer.rejectClaim(id, statusMessage);
		return new ApiResponse<>(HttpStatus.OK, "Claim rejected successfully", claim);
	}

	@GetMapping("/getAllICs")
	public ApiResponse<List<InsuranceCompanyDTO>> getAllIcs() {
		List<InsuranceCompanyDTO> companies = icSer.getAllIcs();
		return new ApiResponse<>(HttpStatus.OK, "Insurance companies retrieved successfully", companies);
	}
	
	@GetMapping("/getHospitalById/{id}")
    public ApiResponse<HospitalDTO> getHospitalById(@PathVariable long id) {
        return icSer.getHospitalById(id);
        
    }
	
	@GetMapping("/getPatientById/{id}")
    public ApiResponse<PatientDTO> getPatientById(@PathVariable long id) {
    	
    	return icSer.getPatientById(id);
    	
    }
    
}
