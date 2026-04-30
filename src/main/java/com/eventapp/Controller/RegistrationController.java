package com.eventapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.eventapp.Service.RegistrationService;
import com.eventapp.dto.ApproveRegistrationRequestDTO;
import com.eventapp.dto.RegistrationRequestDTO;
import com.eventapp.dto.RegistrationResponseDTO;
import com.eventapp.model.Registration;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/registrations")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/registerForEvent")
    public ResponseEntity<RegistrationResponseDTO> registerStudentForEvent(@Valid @RequestBody RegistrationRequestDTO request) {
        return ResponseEntity.ok(registrationService.registerStudentForEvent(request));  
    } 
    

    @GetMapping("/pending/{eventId}")
    public List<Registration> getPendingRegistrations(@PathVariable long eventId) {
        return registrationService.getPendingRegistrations(eventId);
    }


    @GetMapping("/approved/{eventId}")
    public List<Registration> getApprovedRegistrations(@PathVariable long eventId) {
        return registrationService.getApprovedRegistrations(eventId);
    } 
    
    @GetMapping("/allReg")
    public ResponseEntity<?> getAllRegistrations() {
        List<Registration> list = registrationService.getAllRegistrations();
        return ResponseEntity.ok(list);
    }
    
    @PutMapping("/approve")
    public ResponseEntity<byte[]> approveStudent(@Valid @RequestBody ApproveRegistrationRequestDTO request) {

        byte[] pdf = registrationService.approveStudent(request);  

        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=approval_receipt.pdf")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdf);
    }
    
}