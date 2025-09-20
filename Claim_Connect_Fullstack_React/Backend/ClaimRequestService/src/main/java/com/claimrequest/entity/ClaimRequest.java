package com.claimrequest.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

@Data
@Entity
public class ClaimRequest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	
	@Lob
	String synopsis;
	double price;
	long patientId;
	long hospitalId;
	long insuranceCompId;
	String status;
	
	@Lob
	String statusMessage;
	String lastUpdatedBy;
}
