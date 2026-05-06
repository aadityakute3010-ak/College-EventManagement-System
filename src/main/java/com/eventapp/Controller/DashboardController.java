package com.eventapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventapp.Service.AdminService;


@RestController
@RequestMapping("/dashboard")
public class DashboardController {
	
	@Autowired
	private AdminService adminService;
	
	@GetMapping
	public ResponseEntity<?> getDashboard() {
	    return ResponseEntity.ok(adminService.getDashboardData());
	}
	
} 
 