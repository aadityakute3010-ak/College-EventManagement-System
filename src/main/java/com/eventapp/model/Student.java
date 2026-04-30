package com.eventapp.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore; 

@Entity
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer studentId;  
	
	private String studentName;
	
	private String email;
	
	private String password;
	
	private String role = "STUDENT";
	
	@OneToMany(mappedBy = "student",cascade = CascadeType.ALL,orphanRemoval = true) 
	@JsonIgnore 
	private List<Registration> registrations; 
	
	private String otp;
	private LocalDateTime otpExpiry; 

	public Student() {
		super();
		// TODO Auto-generated constructor stub
	}	


	public Student(Integer studentId, String studentName, String email, String password, String role,
			List<Registration> registrations) {
		super();
		this.studentId = studentId;
		this.studentName = studentName;
		this.email = email;
		this.password = password;
		this.role = role;
		this.registrations = registrations;
	}




	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	} 

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public List<Registration> getRegistrations() {
		return registrations;
	}

	public void setRegistrations(List<Registration> registrations) {
		this.registrations = registrations;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}


	public String getOtp() {
		return otp;
	}


	public void setOtp(String otp) {
		this.otp = otp;
	}


	public LocalDateTime getOtpExpiry() {
		return otpExpiry;
	}


	public void setOtpExpiry(LocalDateTime otpExpiry) {
		this.otpExpiry = otpExpiry;
	}	
	
	
	
}
