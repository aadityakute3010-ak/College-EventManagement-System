package com.eventapp.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventapp.dto.EventDashBoardResponseDTO;
import com.eventapp.model.Event;
import com.eventapp.model.Registration;
import com.eventapp.model.Status;
import com.eventapp.model.Student;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> { 

	boolean existsByStudentStudentIdAndEventEventId(long studentId, long eventId); 

	Optional<Registration> findByStudentStudentIdAndEventEventId(long studentId, long eventId);

	List<Registration> findByEventEventIdAndStatus(long eventId, Status status);

	long countByStatus(Status status);

	@Query("SELECT r.event.eventName, COUNT(r) FROM Registration r GROUP BY r.event.eventName")
	List<Object[]> countRegistrationsPerEvent();

	long countByEventEventId(long eventId);

	long countByStudent(Student student);

	long countByStudentAndStatus(Student student, Status status);

	@Query("SELECT r.event FROM Registration r WHERE r.student = :student")
	List<Event> findEventsByStudent(@Param("student") Student student);

	@Query("SELECT new com.eventapp.dto.EventDashBoardResponseDTO("
			+ "e.eventId, e.eventName, e.eventDesc, e.eventDate, e.category) " + "FROM Registration r JOIN r.event e "
			+ "WHERE r.student = :student")
	List<EventDashBoardResponseDTO> findStudentEvents(@Param("student") Student student);
	
	List<Registration> findByStudentStudentId(long studentId);
	
	@Query("SELECT r FROM Registration r")
	List<Registration> findAllRegistrations();

}
