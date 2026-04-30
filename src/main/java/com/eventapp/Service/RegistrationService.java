package com.eventapp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.eventapp.Dao.EventRepository;
import com.eventapp.Dao.RegistrationRepository;
import com.eventapp.Dao.StudentRepository;
import com.eventapp.dto.ApproveRegistrationRequestDTO;
import com.eventapp.dto.RegistrationRequestDTO;
import com.eventapp.dto.RegistrationResponseDTO;
import com.eventapp.model.*;

import jakarta.transaction.Transactional;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepo;  

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private EventRepository eventRepo;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private PdfService pdfService; 
    
    public RegistrationResponseDTO registerStudentForEvent(RegistrationRequestDTO request) {   
    	
    	long studentId = request.getStudentId();
    	long eventId = request.getEventId(); 
    	
        if(registrationRepo.existsByStudentStudentIdAndEventEventId(studentId, eventId)) {
            throw new RuntimeException("Already registered");
        }      
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));       
        long count = registrationRepo.countByEventEventId(eventId);       
        if(event.getMaxParticipants() != null 
                && event.getMaxParticipants() > 0
                && count >= event.getMaxParticipants()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Sorry, the event is full"); 
        }        
        Registration registration = new Registration();
        registration.setStudent(student);
        registration.setEvent(event);
        student.getRegistrations().add(registration);
        event.getRegistrations().add(registration);       
        Registration savedRegistration = registrationRepo.save(registration);       
        emailService.sendRegistrationSuccessEmail(student, event); 
        return new RegistrationResponseDTO(
                savedRegistration.getId(),
                "Event registration successful"
        ); 
    }  
    
    
    public List<Registration> getPendingRegistrations(long eventId) {
        return registrationRepo.findByEventEventIdAndStatus(
            eventId,
            Status.PENDING);
    }
    
    public List<Registration> getApprovedRegistrations(long eventId) {
        return registrationRepo.findByEventEventIdAndStatus(eventId,Status.APPROVED); 
    }
    
    public List<Registration> getAllRegistrations() { 	
        return registrationRepo.findAll(); 
    }

    
    @Transactional 
    public byte[] approveStudent(ApproveRegistrationRequestDTO request) { 

        Integer studentId = request.getStudentId();
        Integer eventId = request.getEventId();

        Registration reg = registrationRepo
                .findByStudentStudentIdAndEventEventId(studentId, eventId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        if (reg.getStatus() != Status.PENDING) {
            throw new RuntimeException("Already approved or invalid status");
        }

        Event event = reg.getEvent();
        Student student = reg.getStudent();

        reg.setPaymentStatus(PaymentStatus.SUCCESS);
        reg.setPaymentId("PAY" + System.currentTimeMillis());
        reg.setStatus(Status.APPROVED);

        event.setTotalEarnings(
                event.getTotalEarnings() + event.getRegistrationFees()
        );

        registrationRepo.save(reg);
        eventRepo.save(event);

   
        String razorpayOrderId = "ADMIN_APPROVED";
        LocalDateTime paymentTime = LocalDateTime.now();

        String invoiceNumber = "INV-" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        
        byte[] pdf = pdfService.generateReceipt(
                reg,
                razorpayOrderId,
                paymentTime,
                invoiceNumber
        );
       
        emailService.sendPaymentSuccessMail(
                student.getEmail(),
                student.getStudentName(),
                event.getEventName(),
                event.getEventDate().toString(),
                event.getRegistrationFees(),
                pdf
        );
        

        return pdf;
    }
        
    
}