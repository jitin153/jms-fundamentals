package com.jitin.jms.demo.msgfiltering.model;

import java.io.Serializable;

public class Claim implements Serializable {

	private static final long serialVersionUID = 5829446774765204487L;

	private int id;
	private int hospitalId;
	private String doctorName;
	private String insuranceProvider;
	private Double claimAmount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(int hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getInsuranceProvider() {
		return insuranceProvider;
	}

	public void setInsuranceProvider(String insuranceProvider) {
		this.insuranceProvider = insuranceProvider;
	}

	public Double getClaimAmount() {
		return claimAmount;
	}

	public void setClaimAmount(Double claimAmount) {
		this.claimAmount = claimAmount;
	}

	@Override
	public String toString() {
		return "Claim [id=" + id + ", hospitalId=" + hospitalId + ", doctorName=" + doctorName + ", insuranceProvider="
				+ insuranceProvider + ", claimAmount=" + claimAmount + "]";
	}

}
