package com.eventapp.model;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore; 

@Entity
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer eventId; 
	
	private String eventName;
	
	private String eventDesc; 
	
	private LocalDate eventDate; 
	
	private String organizerName; 
	
	
	private Integer maxParticipants; 
	
	@NotNull
	@Column(nullable = false)
	private Double registrationFees;
	private Double totalEarnings = 0.0;
	
	
	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL) 
	@JsonIgnore 
	private List<Registration> registrations;
	
	@Enumerated(EnumType.STRING) 
	private EventCategory category;
	
	private String venue;

	@Column(length = 500)
	private String locationLink;
	

	public Event() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Event(Integer eventId, String eventName, String eventDesc, LocalDate eventDate, String organizerName,
			Integer maxParticipants, @NotNull Double registrationFees, Double totalEarnings,
			List<Registration> registrations, EventCategory category) {
		super();
		this.eventId = eventId;
		this.eventName = eventName;
		this.eventDesc = eventDesc;
		this.eventDate = eventDate;
		this.organizerName = organizerName;
		this.maxParticipants = maxParticipants;
		this.registrationFees = registrationFees;
		this.totalEarnings = totalEarnings;
		this.registrations = registrations;
		this.category = category;
	}



	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;  
	}

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

	public List<Registration> getRegistrations() {
		return registrations;
	}

	public void setRegistrations(List<Registration> registrations) {
		this.registrations = registrations;
	}

	public Integer getMaxParticipants() {
		return maxParticipants;
	}

	public void setMaxParticipants(Integer maxParticipants) {
		this.maxParticipants = maxParticipants;
	}

	public Double getRegistrationFees() {
		return registrationFees;
	}

	public void setRegistrationFees(Double registrationFees) {
		this.registrationFees = registrationFees;
	}

	public Double getTotalEarnings() {
		return totalEarnings;
	}

	public void setTotalEarnings(Double totalEarnings) {
		this.totalEarnings = totalEarnings;
	}

	public EventCategory getCategory() {
		return category;
	}

	public void setCategory(EventCategory category) {
		this.category = category;
	}
	
	public String getVenue() {
	    return venue;
	}

	public void setVenue(String venue) {
	    this.venue = venue;
	}

	public String getLocationLink() {
	    return locationLink;
	}

	public void setLocationLink(String locationLink) {
	    this.locationLink = locationLink;
	}
	
	
}
