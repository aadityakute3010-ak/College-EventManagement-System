package com.eventapp.Dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventapp.model.Admin;

@Repository 
public interface AdminRepository extends JpaRepository<Admin, Long> {

	boolean existsByEmailId(String emailId);
	Optional<Admin> findByEmailIdAndPassword(String emailId, String password); 
	
	Optional<Admin> findByEmailId(String email);
	
}
  