package com.eventapp.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Registration {

	@Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; 

    @ManyToOne
    @JoinColumn(name = "studentId")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "eventId") 
    private Event event;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING; // PENDING, APPROVED

    private LocalDateTime registeredAt = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    
    private String paymentId;

	public Registration(long id, Student student, Event event, Status status, LocalDateTime registeredAt) {
		super();
		this.id = id;
		this.student = student;
		this.event = event;
		this.status = status;
		this.registeredAt = registeredAt; 
	}
 
	public Registration() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id; 
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status; 
	}

	public LocalDateTime getRegisteredAt() {
		return registeredAt;
	}

	public void setRegisteredAt(LocalDateTime registeredAt) {
		this.registeredAt = registeredAt;
	}


	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	} 
	
	
    
	
}
