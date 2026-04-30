package com.eventapp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import com.eventapp.Dao.StudentRepository;
import com.eventapp.dto.AddStudentDTO;
import com.eventapp.dto.AddStudentResponseDTO;
import com.eventapp.dto.ForgotPasswordRequestDTO;
import com.eventapp.dto.ResetPasswordDTO;
import com.eventapp.dto.StudentLoginDTO;
import com.eventapp.dto.UpdateEmailDTO;
import com.eventapp.dto.UpdatePasswordDTO;
import com.eventapp.model.Student;
import com.eventapp.security.JwtUtil;

@Service
public class StudentService {

	@Autowired
	private StudentRepository studentRepo;

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailService emailService;

	public AddStudentResponseDTO saveStudent(AddStudentDTO student) {
		if (studentRepo.existsByEmail(student.getEmail())) {
			throw new RuntimeException("Email already exists");
		}
		Student saveStudent = new Student();
		saveStudent.setStudentName(student.getStudentName());
		saveStudent.setEmail(student.getEmail());
		student.setPassword(passwordEncoder.encode(student.getPassword())); 
		saveStudent.setRole("STUDENT");

		AddStudentResponseDTO studentReponse = new AddStudentResponseDTO(saveStudent.getStudentId(),
				saveStudent.getStudentName(), saveStudent.getEmail(), "Student signed up successfully");
		return studentReponse; 
	}

	public List<Student> getAllStudents() {
		return studentRepo.findAll();
	}

	public Student getStudentById(long id) {
		return studentRepo.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
	}

	public void deleteStudent(long id) {
		Student student = studentRepo.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
		studentRepo.delete(student);
	}

	public Map<String, String> loginStudent(StudentLoginDTO student) {
		
		String email = student.getEmail();
    	String password = student.getPassword();
    	
		Student loginStudent = studentRepo.findByEmail(email)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid credentials"));
		
		if (!passwordEncoder.matches(password, loginStudent.getPassword())) {
	        throw new ResponseStatusException(
	                HttpStatus.BAD_REQUEST, "Invalid Password");
	    }
		
		String token = jwtUtil.generateToken(loginStudent.getEmail(), loginStudent.getRole());
		return Map.of("message", "Welcome " + loginStudent.getStudentName(), "token", token);
	}
	
	public String updateEmail(Authentication authentication, UpdateEmailDTO request) {

	    String email = authentication.getName();

	    Student student = studentRepo.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("Student not found"));

	    student.setEmail(request.getNewEmail());

	    studentRepo.save(student);

	    return "Email updated successfully";
	}
	
	public String updatePassword(Authentication authentication, UpdatePasswordDTO request) {

	    String email = authentication.getName();

	    Student student = studentRepo.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("Student not found"));

	    
	    if (!passwordEncoder.matches(request.getOldPassword(), student.getPassword())) {
	        throw new RuntimeException("Old password is incorrect");
	    }

	    String encodedPassword = passwordEncoder.encode(request.getNewPassword());

	    student.setPassword(encodedPassword);

	    studentRepo.save(student);

	    return "Password updated successfully";
	}
	
	public String sendOtp(ForgotPasswordRequestDTO request) {

	    Student student = studentRepo.findByEmail(request.getEmail())
	            .orElseThrow(() -> new RuntimeException("Student not found"));

	    String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

	    student.setOtp(otp);
	    student.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

	    studentRepo.save(student);

	    emailService.sendOtpMail(
	            student.getEmail(),
	            student.getStudentName(),
	            otp
	    ); 
	    return "OTP sent to email";
	} 
	
	public String resetPassword(ResetPasswordDTO request) { 

		Student student = studentRepo.findByEmail(request.getEmail())
	            .orElseThrow(() -> new RuntimeException("Student not found"));
		

	    if (student.getOtp() == null || !student.getOtp().equals(request.getOtp())) {
	        throw new RuntimeException("Invalid OTP");
	    }

	    if (student.getOtpExpiry().isBefore(LocalDateTime.now())) {
	        throw new RuntimeException("OTP expired");
	    }

	    student.setPassword(passwordEncoder.encode(request.getNewPassword()));

	    student.setOtp(null);
	    student.setOtpExpiry(null);

	    studentRepo.save(student);

	    return "Password reset successful";
	}
 
}
