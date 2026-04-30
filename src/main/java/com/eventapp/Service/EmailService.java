package com.eventapp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.eventapp.model.Event;
import com.eventapp.model.Student;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	public void sendRegistrationSuccessEmail(Student student, Event event) {

		try {
			String subject = "Event Registration Successful";

			String body = "<html><body>" + "<p>Dear <b>" + student.getStudentName() + "</b>,</p>"
					+ "<p>Your registration for the event <b>\"" + event.getEventName() + "\"</b> " + "scheduled on <b>"
					+ event.getEventDate() + "</b> has been successfully <b>approved</b>.</p>"
					+ "<p>Your payment has been received, and your seat is now confirmed.</p>"
					+ "<p>If you have any questions, feel free to contact us at " + "<b>support@eventapp.com</b>.</p>"
					+ "<p>We look forward to your participation. Best of luck!</p>" + "<p>Regards,<br>"
					+ "<b>Event Management Team</b><br>" + "support@eventapp.com" + "</p>" + "</body></html>";

			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setFrom("2004adityakute@gmail.com"); 
			helper.setTo(student.getEmail()); 
			helper.setSubject(subject); 
			helper.setText(body, true); 

			mailSender.send(mimeMessage);
		} catch (MailException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public void sendApprovalEmail(Student student, Event event) {

		String subject = "Event Registration Approved";

		String body = "Dear " + student.getStudentName() + ",\n\n"
				+ "We are pleased to inform you that your registration for the event \"" + event.getEventName()
				+ "\" scheduled on " + event.getEventDate() + " has been successfully approved.\n\n"
				+ "You are now confirmed to participate in the event. Kindly ensure your presence on the scheduled date.\n\n"
				+ "We look forward to your participation.\n\n" + "Regards,\nEvent Management Team";

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("2004adityakute@gmail.com");
		message.setTo(student.getEmail());
		message.setSubject(subject);
		message.setText(body);

		mailSender.send(message);
	}

	@Async
	public void sendEventMail(String toEmail, String studentName, String eventName, String eventDate) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject("🎉 New Event Just Dropped!");
		message.setText("Hi " + studentName + ",\n\n" + "We’ve got something exciting for you! 🎊\n\n"
				+ "A new event \"" + eventName + "\" has just been added to the College Event Portal.\n\n" + "📅 Date: "
				+ eventDate + "\n" + "📍 Don’t miss out—limited seats available!\n\n"
				+ "Log in now and secure your spot.\n\n" + "See you there! 🚀\n" + "— Team Event Management");
		mailSender.send(message);
	}
	

	public void sendPaymentSuccessMail(String toEmail, String studentName, String eventName, String eventDate,
			Double amount, byte[] pdf) {

		try {
			String subject = "Payment Successful & Registration Approved";

			String body = "<html><body>" + "<p>Dear <b>" + studentName + "</b>,</p>" +

					"<p>Your payment for the event <b>\"" + eventName + "\"</b> " + "scheduled on <b>" + eventDate
					+ "</b> has been successfully <b>completed</b>.</p>" +

					"<p><b>Payment Details:</b><br>" + "Amount Paid: ₹" + amount + "<br>" + "Status: SUCCESS</p>" +

					"<p>Your registration has been <b>approved</b> and your seat is now confirmed.</p>" +

					"<p>Please find your <b>payment receipt attached</b> with this email.</p>" +

					"<p>If you have any questions, feel free to contact us at " + "<b>support@eventapp.com</b>.</p>" +

					"<p>We look forward to your participation. Best of luck!</p>" +

					"<p>Regards,<br>" + "<b>Event Management Team</b><br>" + "support@eventapp.com<br>"
					+ "+91 9876543210" + "</p>" +

					"</body></html>";

			MimeMessage mimeMessage = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

			helper.setFrom("2004adityakute@gmail.com");
			helper.setTo(toEmail);
			helper.setSubject(subject);
			helper.setText(body, true);

			helper.addAttachment("Payment_Receipt.pdf", new ByteArrayResource(pdf)); 

			mailSender.send(mimeMessage);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to send payment success email");
		}
	}
	
	public void sendHtmlMail(String to, String subject, String htmlContent) {

	    MimeMessage message = mailSender.createMimeMessage();

	    try {
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);

	        helper.setTo(to);
	        helper.setSubject(subject);
	        helper.setText(htmlContent, true);

	        mailSender.send(message);

	    } catch (Exception e) {
	        throw new RuntimeException("Error sending email: " + e.getMessage());
	    }
	}
	
	public void sendOtpMail(String toEmail, String name, String otp) {

	    String subject = "Password Reset OTP";

	    String htmlContent = """
	        <html>
	        <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
	            <div style="max-width: 500px; margin: auto; background: white; padding: 20px; border-radius: 10px;">

	                <h2 style="color: #333;">Hello %s,</h2>

	                <p>We received a request to reset your password.</p>

	                <p style="font-size: 18px;">Your OTP is:</p>

	                <div style="font-size: 28px; font-weight: bold; color: #2c3e50; text-align: center; margin: 20px 0;">
	                    %s
	                </div>

	                <p>This OTP is valid for <b>5 minutes</b>.</p>

	                <p>If you did not request this, please ignore this email.</p>

	                <br>

	                <p style="color: gray;">Regards,<br>Event Management Team</p>
	            </div>
	        </body>
	        </html>
	        """.formatted(name, otp);

	    sendHtmlMail(toEmail, subject, htmlContent);
	}

	
	
}
