package com.eventapp.dto;

import java.time.LocalDate;

import com.eventapp.model.EventCategory;

public class EventDashBoardResponseDTO {

	private Integer eventId;
    private String eventName;
    private String eventDesc;
    private LocalDate eventDate;
    private EventCategory category; 

	public EventDashBoardResponseDTO(Integer eventId, String eventName, String eventDesc, LocalDate eventDate,
			EventCategory category) {
		super();
		this.eventId = eventId;
		this.eventName = eventName;
		this.eventDesc = eventDesc;
		this.eventDate = eventDate;
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


	public EventCategory getCategory() {
		return category;
	}


	public void setCategory(EventCategory category) {
		this.category = category;
	}

    
    
    
	
}
