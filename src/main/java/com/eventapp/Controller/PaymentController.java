package com.eventapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.eventapp.Service.PaymentService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/payment")
public class PaymentController {

	@Autowired
    private PaymentService paymentService;
	
	@PostMapping("/pay/{registrationId}")
	public ResponseEntity<byte[]> makePayment(@PathVariable long registrationId) {

	    byte[] pdf = paymentService.processPayment(registrationId);

	    return ResponseEntity.ok()
	            .header("Content-Disposition", "inline; filename=receipt.pdf")
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(pdf);
	}
	  
} 
