package com.eventapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AddStudentDTO {

	@NotBlank(message = "Student name is required")
	private String studentName;

	@NotBlank(message = "Email Id is required")
	@Email(message = "Invalid email format") 
	private String email;

	@NotBlank(message = "Password cannot be empty")
	private String password;

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
	
	

}
