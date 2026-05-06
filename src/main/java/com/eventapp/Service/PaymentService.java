package com.eventapp.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.eventapp.Dao.EventRepository;
import com.eventapp.Dao.RegistrationRepository;
import com.eventapp.model.Event;
import com.eventapp.model.PaymentStatus;
import com.eventapp.model.Registration;
import com.eventapp.model.Status;
import com.eventapp.model.Student;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import jakarta.transaction.Transactional;

@Service
public class PaymentService {

	@Autowired
    private RazorpayClient razorpayClient;

    @Autowired
    private RegistrationRepository registrationRepo;

    @Autowired
    private EventRepository eventRepo;
    
    @Autowired
    private PdfService pdfService;

    @Autowired
    private EmailService emailService;
    
    
    @Transactional
    public byte[] processPayment(long registrationId) {

        Registration reg = registrationRepo.findById(registrationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registration not found"));
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        if (!reg.getStudent().getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your registration");
        }

        if (reg.getStatus() != Status.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already paid or invalid status");
        }

        Event event = reg.getEvent();
        Student student = reg.getStudent();

        String razorpayOrderId;

        try {
            JSONObject options = new JSONObject();
            options.put("amount", event.getRegistrationFees() * 100);
            options.put("currency", "INR");
            options.put("receipt", "txn_" + registrationId);

            Order order = razorpayClient.orders.create(options);

            razorpayOrderId = order.get("id").toString(); 

        } catch (Exception e) {
            throw new RuntimeException("Error creating Razorpay order");
        }

        String paymentId = "pay_" + System.currentTimeMillis();
        LocalDateTime paymentTime = LocalDateTime.now();
        String invoiceNumber = "INV-" + LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")); 

        reg.setPaymentStatus(PaymentStatus.SUCCESS);
        reg.setPaymentId(paymentId);
        reg.setStatus(Status.APPROVED);

        event.setTotalEarnings(
                event.getTotalEarnings() + event.getRegistrationFees()
        );

        registrationRepo.save(reg);
        eventRepo.save(event);
        
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
                pdf,
                event.getVenue(),
                event.getLocationLink()
        ); 
        
        return pdf;
    }
	
}
