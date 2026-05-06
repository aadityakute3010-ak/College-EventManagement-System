package com.eventapp.dto;

import java.time.LocalDate;

import com.eventapp.model.EventCategory;

public class EventResponseDTO {

	private Integer eventId;
	private String eventName;
	private LocalDate eventDate;
	private String message;
	private EventCategory category;
	private String venue;
	private String locationLink;

	public EventResponseDTO(Integer eventId, String eventName, LocalDate eventDate, String message,
			EventCategory category, String venue, String locationLink) {
		this.eventId = eventId;
		this.eventName = eventName;
		this.eventDate = eventDate;
		this.message = message;
		this.category = category;
		this.venue = venue;
		this.locationLink = locationLink;
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

	public LocalDate getEventDate() {
		return eventDate;
	}

	public void setEventDate(LocalDate eventDate) {
		this.eventDate = eventDate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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
