package com.hospital.controller;

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

import com.hospital.dto.ApiResponse;
import com.hospital.dto.HospitalDTO;
import com.hospital.dto.HospitalPasswordDTO;
import com.hospital.dto.HospitalUpdateDTO;
import com.hospital.dto.InsuranceCompanyDTO;
import com.hospital.dto.PatientDTO;
import com.hospital.entity.ClaimRequest;
import com.hospital.entity.Hospital;
import com.hospital.repo.HospitalRepository;
import com.hospital.service.HospitalService;
import com.hospital.utils.JwtUtils;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/hospital")
@AllArgsConstructor
public class HospitalController {
    // Service layer dependency for handling hospital-related operations
    private HospitalService hosSer;
    
    private AuthenticationManager authManager;
    
    private HospitalRepository hospitalRepo;
    
    private JwtUtils jwtUtils;

    /**
     * Registers a new hospital.
     *
     * @param hospital The hospital entity to be registered.
     * @return ApiResponse containing the registered HospitalDTO and a success message.
     */
    @PostMapping("/signup")
    public ApiResponse<HospitalDTO> registerHospital(@Valid @RequestBody Hospital hospital) {
        HospitalDTO registeredHospital = hosSer.registerHospital(hospital);
        return new ApiResponse<>(HttpStatus.CREATED, "Hospital registered successfully", registeredHospital);
    }
    
    
    
    @PostMapping("/authenticate")
    public ApiResponse<String> authenticateAndGetToken(@RequestBody Hospital hospital) {
    	System.out.println("reaches heree");
    	Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(hospital.getHospitalEmail(), hospital.getHospitalPwd()));
    	
    	if(authentication.isAuthenticated()) {
    		
    		Hospital foundUser = hospitalRepo.findByHospitalEmail(hospital.getHospitalEmail()).orElse(null);
    		
    		String token = jwtUtils.generateToken(foundUser.getHospitalEmail(), foundUser.getHospitalId(), "Hospital");
    		return new ApiResponse<>(HttpStatus.FOUND, "Hospital signed-in successfully", token);
    	}else {
    		throw new UsernameNotFoundException("Invalid User Request!!");
    	}
    }
    

    /**
     * Retrieves a hospital by their ID.
     *
     * @param id The unique identifier of the hospital.
     * @return ApiResponse containing the HospitalDTO and a success message.
     */
    @GetMapping("/getHospital")
    public ApiResponse<HospitalDTO> getHospital(@RequestHeader("userId") String userId) {
    	Long id = Long.parseLong(userId);
        HospitalDTO hospital = hosSer.getHospitalById(id);
        return new ApiResponse<>(HttpStatus.OK, "Hospital retrieved successfully", hospital);
    }
    
    @GetMapping("/getHospitalById/{id}")
    public ApiResponse<HospitalDTO> getHospital(@PathVariable long id) {
        HospitalDTO hospital = hosSer.getHospitalById(id);
        return new ApiResponse<>(HttpStatus.OK, "Hospital retrieved successfully", hospital);
    }

    /**
     * Updates an existing hospital's information.
     *
     * @param hospital The hospital entity with updated information.
     * @return ApiResponse containing the updated HospitalDTO and a success message.
     */
    @PutMapping("/updateHospital")
    public ApiResponse<HospitalDTO> updateHospital(@RequestHeader("userId") String userId,  @Valid @RequestBody HospitalUpdateDTO hospital) {
    	long id = Long.parseLong(userId);
        HospitalDTO updatedHospital = hosSer.updateHospital(id, hospital);
        return new ApiResponse<>(HttpStatus.OK, "Hospital updated successfully", updatedHospital);
    }
    
    @PutMapping("/updatePassword")
    public ApiResponse<HospitalDTO> updatePassword(@RequestHeader("userId") String userId,  @Valid @RequestBody HospitalPasswordDTO hospital) {
    	long id = Long.parseLong(userId);
        HospitalDTO updatedHospital = hosSer.updatePassword(id, hospital);
        return new ApiResponse<>(HttpStatus.OK, "Hospital password updated successfully", updatedHospital);
    }
    
    

    /**
     * Deletes a hospital by their ID.
     *
     * @param id The unique identifier of the hospital to be deleted.
     * @return ApiResponse containing a success message.
     */
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteHospital(@PathVariable Long id) {
        hosSer.deleteHospital(id);
        return new ApiResponse<>(HttpStatus.OK, "Hospital deleted successfully", "Success");
    }

    /**
     * Authenticates a hospital using email and password.
     *
     * @param email The hospital's email address.
     * @param pwd   The hospital's password.
     * @return ApiResponse containing the HospitalDTO and a success message.
     */
    @PostMapping("/login")
    public ApiResponse<HospitalDTO> loginHospital(@RequestParam String email, @RequestParam String pwd) {
        HospitalDTO hospital = hosSer.loginHospital(email, pwd);
        return new ApiResponse<>(HttpStatus.OK, "Login Success", hospital);
    }

    /**
     * Generates a new claim request.
     *
     * @param cr The ClaimRequest entity to be generated.
     * @return ApiResponse containing the generated ClaimRequest and a success message.
     */
    @PostMapping("/addClaim")
    public ApiResponse<ClaimRequest> generateClaimRequest(@RequestBody ClaimRequest cr, @RequestHeader("userId") String userId) {
    	long hospitalId = Long.parseLong(userId);
        ClaimRequest claim = hosSer.generateClaimReq(cr,hospitalId);
        return new ApiResponse<>(HttpStatus.CREATED, "Claim generated successfully", claim);
    }

    /**
     * Updates an existing claim request.
     *
     * @param cr The ClaimRequest entity with updated information.
     * @return ApiResponse containing the updated ClaimRequest and a success message.
     */
    @PutMapping("/updateClaim")
    public ApiResponse<ClaimRequest> updateClaimRequest(@RequestBody ClaimRequest cr) {
        ClaimRequest claim = hosSer.updateClaimReq(cr);
        return new ApiResponse<>(HttpStatus.OK, "Claim updated successfully", claim);
    }

    /**
     * Retrieves all claim requests associated with a hospital's ID.
     *
     * @param id The unique identifier of the hospital.
     * @return ApiResponse containing the list of ClaimRequest objects and a success message.
     */
    @GetMapping("/getClaimsByHosId")
    public ApiResponse<List<ClaimRequest>> getClaimsByHospitalId(@RequestHeader("userId") String userId) {
    	Long id = Long.valueOf(userId);
        List<ClaimRequest> claims = hosSer.getClaimsByHospitalId(id);
        return new ApiResponse<>(HttpStatus.OK, "Claims retrieved successfully", claims);
    }
    
//    @GetMapping("/getClaimsByHosId/{id}")
//    public ApiResponse<List<ClaimRequest>> getClaimsByHospitalId(@PathVariable long id) {
//        List<ClaimRequest> claims = hosSer.getClaimsByHospitalId(id);
//        return new ApiResponse<>(HttpStatus.OK, "Claims retrieved successfully", claims);
//    }
    
    
    
    @GetMapping("/checkEligibility/{patientId}")
    public boolean checkEligibility(@PathVariable long patientId) {
    	return hosSer.checkEligibility(patientId);
    }
    
    
    @GetMapping("/getPatientByEmail")
    public ApiResponse<PatientDTO> getPatientByEmail(@RequestParam String email) {
    	
    	return hosSer.getPatientByEmail(email);
    	
    }
    
    @GetMapping("/getPatientById/{id}")
    public ApiResponse<PatientDTO> getPatientById(@PathVariable long id) {
    	
    	return hosSer.getPatientById(id);
    	
    }
    
    @GetMapping("/getIcById/{id}")
    public ApiResponse<InsuranceCompanyDTO> getIcById(@PathVariable long id) {
    	
    	return hosSer.getIcById(id);
    	
    }
}
