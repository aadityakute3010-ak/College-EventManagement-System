package com.eventapp.Dao;

import java.time.LocalDate;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eventapp.model.Event;
import com.eventapp.model.EventCategory;

@Repository 
public interface EventRepository extends JpaRepository<Event, Long> {	
	
	List<Event> findByEventNameContainingIgnoreCase(String name);
	List<Event> findByEventDateAfter(LocalDate date);
	List<Event> findByEventDateBefore(LocalDate date);
	
	@Query("SELECT COALESCE(SUM(e.totalEarnings), 0) FROM Event e")
    double sumTotalEarnings();

    @Query("SELECT e.eventName, e.totalEarnings FROM Event e")
    List<Object[]> getEventWiseEarnings();
    
    List<Event> findByCategory(EventCategory category); 
    
    List<Event> findByCategoryAndEventIdNotIn(
            EventCategory category,
            List<Integer> eventIds
    );
    
}  
 