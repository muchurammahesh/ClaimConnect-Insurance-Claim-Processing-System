package com.hospital.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hospital.repo.HospitalRepository;
import com.hospital.entity.Hospital;
import com.hospital.repo.*;

@Service
public class UserInfoUserDetailsService implements UserDetailsService {
	
	@Autowired
	private HospitalRepository hospitalRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<Hospital> userInfo = hospitalRepository.findByHospitalEmail(username);
		 return userInfo.map(UserInfoUserDetails::new)
	                .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));
	}
	
	

}
