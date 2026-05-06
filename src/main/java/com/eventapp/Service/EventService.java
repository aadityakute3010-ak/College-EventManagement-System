package com.eventapp.Service;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.eventapp.Dao.EventRepository;
import com.eventapp.Dao.RegistrationRepository;
import com.eventapp.Dao.StudentRepository;
import com.eventapp.dto.EventRequestDTO;
import com.eventapp.dto.EventResponseDTO;
import com.eventapp.model.Event;
import com.eventapp.model.EventCategory;
import com.eventapp.model.Registration;
import com.eventapp.model.Student;


@Service
public class EventService { 

	@Autowired
	private EventRepository eventRepo;
	
	@Autowired
	private StudentRepository studentRepo; 
	
	@Autowired 
	private RegistrationRepository registrationRepo;
		
//	@Autowired
//	private EmailService emailService; 
	
	public EventResponseDTO addEvent(EventRequestDTO request) {
	    Event event = new Event();

	    event.setEventName(request.getEventName());
	    event.setEventDesc(request.getEventDesc());
	    event.setEventDate(request.getEventDate());
	    event.setOrganizerName(request.getOrganizerName());
	    event.setRegistrationFees(request.getRegistrationFees());
	    event.setMaxParticipants(request.getMaxParticipants());
	    event.setCategory(request.getCategory());

	    event.setVenue(request.getVenue());
	    event.setLocationLink(request.getLocationLink());

	    Event savedEvent = eventRepo.save(event);

	    return new EventResponseDTO(
	            savedEvent.getEventId(),
	            savedEvent.getEventName(),
	            savedEvent.getEventDate(),
	            "Event added successfully",
	            savedEvent.getCategory(),
	            savedEvent.getVenue(),
	            savedEvent.getLocationLink()
	    );
	} 
	
	public List<Event> getAllEvents() {
	    return eventRepo.findAll(); 
	} 	
	
	public Event getEventById(long eventId) {
	    Optional<Event> event = eventRepo.findById(eventId);

	    if(event.isPresent()) {
	        return event.get();
	    } else {
	        throw new RuntimeException("Event not found");
	    }
	}
	
	public void deleteEvent(long eventId) {
	    if(!eventRepo.existsById(eventId)) {
	        throw new RuntimeException("Event not found");
	    }
	    eventRepo.deleteById(eventId);
	}
	
	public List<String> getRegisteredStudentNames(long eventId) {
	    Event event = eventRepo.findById(eventId)
	            .orElseThrow(() -> new RuntimeException("Event not found"));

	    List<String> names = new ArrayList<>(); 

	    for (Registration r : event.getRegistrations()) {
	        names.add(r.getStudent().getStudentName());
	    }
	    return names;
	}
	
	public List<Event> searchEvents(String name) {
        return eventRepo.findByEventNameContainingIgnoreCase(name);
    }
	
	public List<Event> getUpcomingEvents() {
	    return eventRepo.findByEventDateAfter(LocalDate.now());
	}

	public List<Event> getPastEvents() {
	    return eventRepo.findByEventDateBefore(LocalDate.now()); 
	}
	
	public List<EventResponseDTO> getEventsByCategory(String category) {

	    EventCategory enumCategory =
	            EventCategory.valueOf(category.toUpperCase());

	    List<Event> events = eventRepo.findByCategory(enumCategory);

	    return events.stream()
	            .map(e -> new EventResponseDTO(
	                    e.getEventId(),
	                    e.getEventName(),
	                    e.getEventDate(),
	                    "Event fetched successfully",
	                    e.getCategory(),
	                    e.getVenue(),
	                    e.getLocationLink()
	            ))
	            .toList(); 
	}
	
	public List<EventResponseDTO> getRecommendedEvents(Authentication authentication) {

		String email = authentication.getName();   

	    Student student = studentRepo.findByEmail(email) 
	            .orElseThrow(() -> new RuntimeException("Student not found")); 

	    List<Registration> registrations =
	            registrationRepo.findByStudentStudentId(student.getStudentId());


	    if (registrations.isEmpty()) {
	        return List.of();
	    }

	    List<Integer> registeredEventIds = registrations.stream()
	            .map(r -> r.getEvent().getEventId())
	            .toList();

	    Map<EventCategory, Long> categoryCount =
	            registrations.stream()
	                    .collect(Collectors.groupingBy(
	                            r -> r.getEvent().getCategory(),
	                            Collectors.counting()
	                    ));

	    List<EventCategory> sortedCategories = categoryCount.entrySet().stream()
	            .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
	            .map(Map.Entry::getKey)
	            .toList();

	    List<Event> recommendedEvents = new ArrayList<>();

	    for (EventCategory category : sortedCategories) {
	        List<Event> events =
	                eventRepo.findByCategoryAndEventIdNotIn(category, registeredEventIds);

	        recommendedEvents.addAll(events);
	    }

	    return recommendedEvents.stream()
	    		.map(e -> new EventResponseDTO(
	    			    e.getEventId(),
	    			    e.getEventName(),
	    			    e.getEventDate(),
	    			    "Recommended",
	    			    e.getCategory(),
	    			    e.getVenue(),              
	    			    e.getLocationLink()       
	    			))
	            .toList();
	}  
	 
} 
