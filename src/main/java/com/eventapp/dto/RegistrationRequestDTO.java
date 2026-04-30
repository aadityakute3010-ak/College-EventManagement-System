package com.eventapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class RegistrationRequestDTO {
	
	@NotNull(message = "Please provide student ID")
	@Positive(message = "Student ID must be greater than 0") 
    private Integer studentId;
	
	@NotNull(message = "Please provide event ID")
	@Positive(message = "Event ID must be greater than 0")
    private Integer eventId; 
    
	public Integer getStudentId() {
		return studentId; 
	}
	public void setStudentId(Integer studentId) {
		this.studentId = studentId; 
	}
	public Integer getEventId() {
		return eventId;
	}
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	} 

    
}
