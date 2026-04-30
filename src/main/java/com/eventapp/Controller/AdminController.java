package com.eventapp.Controller; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.eventapp.Service.AdminService;
import com.eventapp.dto.AddAdminRequestDTO;
import com.eventapp.dto.AdminChangePasswordDTO;
import com.eventapp.dto.AdminForgotPasswordDTO;
import com.eventapp.dto.AdminLoginRequestDTO;
import com.eventapp.dto.AdminResetPasswordDTO;
import com.eventapp.model.Admin;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/addAdmin")
    public String addAdmin(@RequestBody AddAdminRequestDTO admin) {
        adminService.addAdmin(admin);
        return "Admin added successfully";
    }

  
    @DeleteMapping("deletAdmin/{adminId}") 
    public String deleteAdmin(@PathVariable long adminId) {
        adminService.deleteAdmin(adminId);
        return "Admin deleted successfully";
    }
    
    //Get admin by ID
    @GetMapping("/{adminId}") 
    public Admin getAdminById(@PathVariable long adminId) {
        return adminService.getAdminById(adminId);
    }

    //Get all admins
    @GetMapping("/all")
    public List<Admin> getAllAdmins() {
        return adminService.getAllAdmins();
    } 


    @PostMapping("/loginAdmin")
    public ResponseEntity<?> loginAdmin(@Valid @RequestBody AdminLoginRequestDTO request) {    
    	Map<String, String> response = adminService.loginAdmin(request); 	  
    	return ResponseEntity.ok(response); 
    } 
    
    @PostMapping("/admin-forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestBody AdminForgotPasswordDTO request) {

        return ResponseEntity.ok(
                adminService.sendAdminOtp(request)
        );
    }
    
    @PostMapping("/admin-reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestBody AdminResetPasswordDTO request) {

        return ResponseEntity.ok(
                adminService.resetAdminPassword(request)
        );
    }
    
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            Authentication authentication,
            @RequestBody AdminChangePasswordDTO request) { 

        return ResponseEntity.ok(
                adminService.changePassword(authentication, request)
        );
    }
    
} 