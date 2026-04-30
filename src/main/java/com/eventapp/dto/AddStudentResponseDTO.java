package com.eventapp.dto;

public class AddStudentResponseDTO {

   private Integer studentId;  
	
	private String studentName;
	
	private String email;
	
	private String message;

	

	public AddStudentResponseDTO(Integer studentId, String studentName, String email, String message) {
		super();
		this.studentId = studentId;
		this.studentName = studentName;
		this.email = email;
		this.message = message;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
