package com.eventapp.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.eventapp.Service.EventService;
import com.eventapp.dto.EventRequestDTO;
import com.eventapp.dto.EventResponseDTO;
import com.eventapp.model.Event;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;
    

    @PostMapping
    public ResponseEntity<EventResponseDTO> addEvent(@Valid @RequestBody EventRequestDTO request) {  
        return ResponseEntity.ok(eventService.addEvent(request));
    } 
 
   
    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents(); 
    }
 
    
    @GetMapping("/{eventId}")
    public Event getEventById(@PathVariable long eventId) {
        return eventService.getEventById(eventId);
    } 

    
    @DeleteMapping("/{eventId}")
    public String deleteEvent(@PathVariable long eventId) {
        eventService.deleteEvent(eventId);
        return "Event deleted successfully";
    }

    
    @GetMapping("/{eventId}/students")
    public List<String> getRegisteredStudentNames(@PathVariable long eventId) {
        return eventService.getRegisteredStudentNames(eventId);
    }
    
    @GetMapping("/search")
    public List<Event> searchEvents(@RequestParam String name) {
        return eventService.searchEvents(name);
    }
    
    @GetMapping("/upcoming")
    public List<Event> upcomingEvents() {
        return eventService.getUpcomingEvents();
    }

    @GetMapping("/past")
    public List<Event> pastEvents() {
        return eventService.getPastEvents();
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<EventResponseDTO>> getByCategory(@Valid @PathVariable String category) {

        return ResponseEntity.ok(eventService.getEventsByCategory(category));
    }
    
    @GetMapping("/recommendations")
    public ResponseEntity<List<EventResponseDTO>> getRecommendations(
            Authentication authentication) {

        return ResponseEntity.ok(
                eventService.getRecommendedEvents(authentication) 
        );
    }
    
    
}