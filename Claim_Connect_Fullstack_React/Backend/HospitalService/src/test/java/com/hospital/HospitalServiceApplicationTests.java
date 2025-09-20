package com.hospital;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.hospital.dto.HospitalDTO;
import com.hospital.entity.Hospital;
import com.hospital.repo.HospitalRepository;
import com.hospital.service.HospitalServiceImpl;

@SpringBootTest
class HospitalServiceApplicationTests {
	
	@Mock
    private HospitalRepository hospitalRepo;
	
	@InjectMocks
    private HospitalServiceImpl hospitalService;
	
	@Test
    void testRegisterHospital() {
        // Arrange
        Hospital hospital = new Hospital();
        hospital.setHospitalName("Test Hospital");
        hospital.setHospitalEmail("test@hospital.com");
        hospital.setHospitalPwd("password123");
        when(hospitalRepo.save(any(Hospital.class))).thenReturn(hospital);

        // Act
        HospitalDTO savedHospital = hospitalService.registerHospital(hospital);

        // Assert
        assertEquals("Test Hospital", savedHospital.getHospitalName());
        assertEquals("test@hospital.com", savedHospital.getHospitalEmail());
    }

}
