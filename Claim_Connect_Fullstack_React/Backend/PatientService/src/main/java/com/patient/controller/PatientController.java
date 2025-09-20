package com.patient.controller;

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

import com.patient.dto.ApiResponse;
import com.patient.dto.HospitalDTO;
import com.patient.dto.InsuranceCompanyDTO;
import com.patient.dto.PatientDTO;
import com.patient.dto.PatientPasswordDTO;
import com.patient.dto.PatientUpdateDTO;
import com.patient.entity.ClaimRequest;
import com.patient.entity.Patient;
import com.patient.repo.PatientRepository;
import com.patient.service.PatientService;
import com.patient.utils.JwtUtils;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/patient")
@AllArgsConstructor
public class PatientController {

    // Service layer dependency for handling patient-related operations
    private PatientService patSer;
    
    private AuthenticationManager authManager;
    
    private PatientRepository patRepo;
    
    private JwtUtils jwtUtils;
    /**
     * Registers a new patient.
     *
     * @param patient The patient entity to be registered.
     * @return ApiResponse containing the registered PatientDTO and a success message.
     */
    @PostMapping("/signup")
    public ApiResponse<PatientDTO> registerPatient(@RequestBody Patient patient) {
        PatientDTO registeredPatient = patSer.registerPatient(patient);
        return new ApiResponse<>(HttpStatus.CREATED, "Patient registered successfully", registeredPatient);
    }
    
    
    @PostMapping("/authenticate")
    public ApiResponse<String> authenticateAndGetToken(@RequestBody Patient patient) {
    	
    	Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(patient.getPatientEmail(), patient.getPatientPassword()));
    	
    	if(authentication.isAuthenticated()) {
    		
    		Patient foundUser = patRepo.findByPatientEmail(patient.getPatientEmail()).orElse(null);
    		
    		String token = jwtUtils.generateToken(foundUser.getPatientEmail(), foundUser.getPatientId(), "Patient");
    		return new ApiResponse<>(HttpStatus.FOUND, "Patient signed-in successfully", token);
    	}else {
    		throw new UsernameNotFoundException("Invalid User Request!!");
    	}
    }

    /**
     * Retrieves a patient by their ID.
     *
     * @param id The unique identifier of the patient.
     * @return ApiResponse containing the PatientDTO and a success message.
     */
    @GetMapping("/getPatient")
    public ApiResponse<PatientDTO> getPatient(@RequestHeader String userId) {
    	long id = Long.parseLong(userId);
        PatientDTO patient = patSer.getPatientById(id);
        return new ApiResponse<>(HttpStatus.OK, "Patient retrieved successfully", patient);
    }
    
    @GetMapping("/getPatientById/{patId}")
    public ApiResponse<PatientDTO> getPatientById(@PathVariable long patId) {
        PatientDTO patient = patSer.getPatientById(patId);
        return new ApiResponse<>(HttpStatus.OK, "Patient retrieved successfully", patient);
    }
    
    @GetMapping("/getPatientByEmail")
    public ApiResponse<PatientDTO> getPatientByEmail(@RequestParam String email) {
    	System.out.println("inside pateint cntrl " + email);
    	
        PatientDTO patient = patSer.getPatientByEmail(email);
        return new ApiResponse<>(HttpStatus.OK, "Patient retrieved successfully", patient);
    }

    /**
     * Updates an existing patient's information.
     *
     * @param patient The patient entity with updated information.
     * @return ApiResponse containing the updated PatientDTO and a success message.
     */
    @PutMapping("/updatePatient")
    public ApiResponse<PatientDTO> updatePatient(@RequestHeader("userId") String userId, @RequestBody PatientUpdateDTO patient) {
    	long id = Long.parseLong(userId);
        PatientDTO updatedPatient = patSer.updatePatient(id, patient);
        return new ApiResponse<>(HttpStatus.OK, "Patient updated successfully", updatedPatient);
    }
    
    
    @PutMapping("/updatePassword")
    public ApiResponse<PatientDTO> updatePassword(@RequestHeader("userId") String userId, @RequestBody PatientPasswordDTO patient) {
    	long id = Long.parseLong(userId);
        PatientDTO updatedPatient = patSer.updatePassword(id, patient);
        return new ApiResponse<>(HttpStatus.OK, "Patient password updated successfully", updatedPatient);
    }

    /**
     * Deletes a patient by their ID.
     *
     * @param id The unique identifier of the patient to be deleted.
     * @return ApiResponse containing a success message.
     */
    @DeleteMapping("/{id}")
    public ApiResponse<String> deletePatient(@PathVariable Long id) {
        patSer.deletePatient(id);
        return new ApiResponse<>(HttpStatus.OK, "Patient deleted successfully", "Success");
    }

    /**
     * Authenticates a patient using email and password.
     *
     * @param email    The patient's email address.
     * @param password The patient's password.
     * @return ApiResponse containing the PatientDTO and a success message.
     */
    @PostMapping("/login")
    public ApiResponse<PatientDTO> loginPatient(@RequestParam String email, @RequestParam String password) {
        PatientDTO patient = patSer.loginPatient(email, password);
        return new ApiResponse<>(HttpStatus.OK, "Login Success", patient);
    }

    /**
     * Retrieves all claims associated with a patient by their ID.
     *
     * @param patientId The unique identifier of the patient.
     * @return ApiResponse containing the list of ClaimRequest objects and a success message.
     */
    @GetMapping("/getClaims")
    public ApiResponse<List<ClaimRequest>> getClaimByPatientId(@RequestHeader String userId) {
    	long patientId = Long.parseLong(userId);
        List<ClaimRequest> claimList = patSer.getClaimsByPatientId(patientId);
        return new ApiResponse<>(HttpStatus.OK, "Successful retrieval of claims list", claimList);
    }

    /**
     * Accepts a claim request by its ID.
     *
     * @param id The unique identifier of the claim.
     * @return ApiResponse containing the updated ClaimRequest and a success message.
     */
    @PutMapping("/acceptClaim/{id}")
    public ApiResponse<ClaimRequest> acceptClaim(@PathVariable long id) {
        ClaimRequest claim = patSer.acceptClaim(id);
        return new ApiResponse<>(HttpStatus.OK, "Claim accepted successfully", claim);
    }

    /**
     * Reverts a claim request by its ID with a provided status message.
     *
     * @param id            The unique identifier of the claim.
     * @param statusMessage The message explaining the reason for revert.
     * @return ApiResponse containing the updated ClaimRequest and a success message.
     */
    @PutMapping("/revertClaim/{id}")
    public ApiResponse<ClaimRequest> revertClaim(@PathVariable long id, @RequestBody String statusMessage) {
        ClaimRequest claim = patSer.revertClaim(id, statusMessage);
        return new ApiResponse<>(HttpStatus.OK, "Claim reverted successfully", claim);
    }

    /**
     * Rejects a claim request by its ID with a provided status message.
     *
     * @param id            The unique identifier of the claim.
     * @param statusMessage The message explaining the reason for rejection.
     * @return ApiResponse containing the updated ClaimRequest and a success message.
     */
    @PutMapping("/rejectClaim/{id}")
    public ApiResponse<ClaimRequest> rejectClaim(@PathVariable long id, @RequestBody String statusMessage) {
        ClaimRequest claim = patSer.rejectClaim(id, statusMessage);
        return new ApiResponse<>(HttpStatus.OK, "Claim rejected successfully", claim);
    }
    
    
    @GetMapping("/getHospitalById/{id}")
    public ApiResponse<HospitalDTO> getHospitalById(@PathVariable long id) {
        return patSer.getHospitalById(id);
        
    }
    
    @GetMapping("/getIcById/{id}")
    public ApiResponse<InsuranceCompanyDTO> getIcById(@PathVariable long id) {
        return patSer.getIcById(id);
        
    }

}
