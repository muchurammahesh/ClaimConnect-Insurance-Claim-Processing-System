package com.insurancecompany.entity;

import lombok.Data;

@Data
public class ClaimRequest {


	long id;
	String synopsis;
	double price;
	long patientId;
	long hospitalId;
	long insuranceCompId;
	String status;
	String statusMessage;
	String lastUpdatedBy;
}

