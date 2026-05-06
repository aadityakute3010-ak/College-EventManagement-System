package com.eventapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.eventapp.Service.StudentService;
import com.eventapp.dto.AddStudentDTO;
import com.eventapp.dto.AddStudentResponseDTO;
import com.eventapp.dto.ForgotPasswordRequestDTO;
import com.eventapp.dto.ResetPasswordDTO;
import com.eventapp.dto.StudentLoginDTO;
import com.eventapp.dto.UpdateEmailDTO;
import com.eventapp.dto.UpdatePasswordDTO;
import com.eventapp.model.Student;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/students")
public class StudentController {

	@Autowired
	private StudentService studentService;

	@PostMapping("/signUp")
	public ResponseEntity<AddStudentResponseDTO> addStudent(@Valid @RequestBody AddStudentDTO student) {
		return ResponseEntity.ok(studentService.saveStudent(student));
	}

	@GetMapping("/allStudents")
	public List<Student> getAllStudents() {
		return studentService.getAllStudents();
	}

	@GetMapping("/{id}")
	public Student getStudentById(@PathVariable long id) {
		return studentService.getStudentById(id);
	}

	@DeleteMapping("/{id}")
	public String deleteStudent(@PathVariable long id) {
		studentService.deleteStudent(id);
		return "Student deleted successfully";
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginStudent(@Valid @RequestBody StudentLoginDTO student) {
		return ResponseEntity.ok(studentService.loginStudent(student));
	}

	@PutMapping("/update-email")
	public ResponseEntity<String> updateEmail(Authentication authentication, @RequestBody UpdateEmailDTO request) {

		return ResponseEntity.ok(studentService.updateEmail(authentication, request));
	}

	@PutMapping("/update-password")
	public ResponseEntity<String> updatePassword(Authentication authentication,
			@RequestBody UpdatePasswordDTO request) {

		return ResponseEntity.ok(studentService.updatePassword(authentication, request));
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {

		return ResponseEntity.ok(studentService.sendOtp(request));
	}

	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO request) {

		return ResponseEntity.ok(studentService.resetPassword(request));
	}  

}
