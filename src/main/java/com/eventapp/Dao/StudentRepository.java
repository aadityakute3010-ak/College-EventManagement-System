package com.eventapp.Dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventapp.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
	
	boolean existsByEmail(String email);
	Optional<Student> findByEmailAndPassword(String email, String password);
	List<Student> findAll(); 
	Optional<Student> findByEmail(String email); 
	
}
