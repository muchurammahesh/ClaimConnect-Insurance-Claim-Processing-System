package com.patient.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.patient.repo.*;
import com.patient.entity.*;

@Service
public class UserInfoUserDetailsService implements UserDetailsService {
	
	@Autowired
	private PatientRepository patRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<Patient> userInfo = patRepo.findByPatientEmail(username);
		 return userInfo.map(UserInfoUserDetails::new)
	                .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));
	}
	
	

}
