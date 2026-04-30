package com.eventapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AddAdminRequestDTO {

	@NotBlank(message = "Admin name is required")
	private String name;

	@NotBlank(message = "Email ID is required")
	@Email(message = "Invalid email format") 
	private String emailId;

	@NotBlank(message = "Password needs to be provided")
	private String password;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
