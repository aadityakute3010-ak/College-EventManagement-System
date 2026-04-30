package com.eventapp.dto;

import java.time.LocalDate;

import com.eventapp.model.EventCategory;

public class EventResponseDTO {

    private Integer eventId;
    private String eventName;
    private LocalDate eventDate;
    private String message;
    private EventCategory category;
 

	public EventResponseDTO(Integer eventId, String eventName, LocalDate eventDate, String message,
			EventCategory category) {
		super();
		this.eventId = eventId;
		this.eventName = eventName;
		this.eventDate = eventDate;
		this.message = message;
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

    
    
}
