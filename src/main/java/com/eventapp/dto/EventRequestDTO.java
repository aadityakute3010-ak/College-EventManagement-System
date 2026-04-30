package com.eventapp.dto;

import java.time.LocalDate;

import com.eventapp.model.EventCategory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class EventRequestDTO {

	@NotBlank(message = "Event name is required")
    private String eventName;
	
	@NotBlank(message = "Event description is required")
    private String eventDesc;
	
	@NotNull(message = "Event date is required")
    private LocalDate eventDate;
	
	@NotBlank(message = "Organizer name is required")
    private String organizerName;
	
	@NotNull(message = "Registration fee is required")
    @Positive(message = "Registration fee must be greater than 0")
    private Double registrationFees;
	
	@NotNull(message = "Max participants is required")
    @Min(value = 1, message = "At least 1 participant required")
    private Integer maxParticipants; 
	
	@NotNull(message = "Category is required")
	private EventCategory category;
    
    
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getEventDesc() {
		return eventDesc;
	}
	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc; 
	} 
	public LocalDate getEventDate() {
		return eventDate;
	}
	public void setEventDate(LocalDate eventDate) {
		this.eventDate = eventDate;
	}
	public String getOrganizerName() {
		return organizerName;
	}
	public void setOrganizerName(String organizerName) {
		this.organizerName = organizerName;
	}
	public Double getRegistrationFees() {
		return registrationFees;
	}
	public void setRegistrationFees(Double registrationFees) {
		this.registrationFees = registrationFees;
	}
	public Integer getMaxParticipants() {
		return maxParticipants;
	}
	public void setMaxParticipants(Integer maxParticipants) {
		this.maxParticipants = maxParticipants;
	}
	public EventCategory getCategory() {
		return category;
	}
	public void setCategory(EventCategory category) {
		this.category = category;
	}

    
}
