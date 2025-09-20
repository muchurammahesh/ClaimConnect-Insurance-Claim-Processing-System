package com.insurancecompany.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.insurancecompany.repo.*;
import com.insurancecompany.entity.*;

@Service
public class UserInfoUserDetailsService implements UserDetailsService {
	
	@Autowired
	private InsuranceCompanyRepository icRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<InsuranceCompany> userInfo = icRepository.findByInsuranceCompEmail(username);
		 return userInfo.map(UserInfoUserDetails::new)
	                .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));
	}
	
	

}
