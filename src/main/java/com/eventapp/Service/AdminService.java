package com.eventapp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.eventapp.Dao.AdminRepository;
import com.eventapp.Dao.EventRepository;
import com.eventapp.Dao.RegistrationRepository;
import com.eventapp.Dao.StudentRepository;
import com.eventapp.dto.AddAdminRequestDTO;
import com.eventapp.dto.AdminChangePasswordDTO;
import com.eventapp.dto.AdminForgotPasswordDTO;
import com.eventapp.dto.AdminLoginRequestDTO;
import com.eventapp.dto.AdminResetPasswordDTO;
import com.eventapp.model.Admin;
import com.eventapp.model.Status;
import com.eventapp.model.Student;
import com.eventapp.security.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AdminService {

	@Autowired
	private AdminRepository adminRepo;

	@Autowired
	private StudentRepository studentRepo;

	@Autowired
	private EventRepository eventRepo;

	@Autowired
	private RegistrationRepository registrationRepo;

	@Autowired
	private EmailService emailService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public void addAdmin(AddAdminRequestDTO admin) {
		if (adminRepo.existsByEmailId(admin.getEmailId())) {
			throw new IllegalArgumentException("Email already exists");
		}
		Admin saveAdmin = new Admin();
		saveAdmin.setName(admin.getName());
		saveAdmin.setEmailId(admin.getEmailId());
		saveAdmin.setPassword(admin.getPassword());
		saveAdmin.setRole("ADMIN");
		adminRepo.save(saveAdmin);
	}

	public void deleteAdmin(long adminId) {
		if (!adminRepo.existsById(adminId)) {
			throw new RuntimeException("Admin not found");
		}
		adminRepo.deleteById(adminId);
	}

	public Admin getAdminById(long adminId) {
		return adminRepo.findById(adminId).orElseThrow(() -> new RuntimeException("Admin not found"));
	}

	public List<Admin> getAllAdmins() {
		return adminRepo.findAll();
	}

	public Map<String, String> loginAdmin(AdminLoginRequestDTO request) {

		Admin admin = adminRepo.findByEmailId(request.getEmailId())
				.orElseThrow(() -> new RuntimeException("Invalid credentials"));

		if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
			throw new RuntimeException("Invalid credentials");
		}

		String token = jwtUtil.generateToken(admin.getEmailId(), admin.getRole());
		return Map.of("message", "Welcome Admin : " + admin.getName(), "token", token);
	}

	public Map<String, Object> getDashboardData() {

		Map<String, Object> dashboard = new HashMap<>();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String role = auth.getAuthorities().iterator().next().getAuthority();
		String email = auth.getName();
		if (role.equals("ADMIN")) {

			dashboard.put("totalStudents", studentRepo.count());
			dashboard.put("totalEvents", eventRepo.count());
			dashboard.put("totalRegistrations", registrationRepo.count());
			dashboard.put("approvedRegistrations", registrationRepo.countByStatus(Status.APPROVED));
			dashboard.put("pendingRegistrations", registrationRepo.countByStatus(Status.PENDING));
			Map<String, Long> eventStats = new HashMap<>();
			List<Object[]> data = registrationRepo.countRegistrationsPerEvent();
			for (Object[] obj : data) {
				eventStats.put((String) obj[0], (Long) obj[1]);
			}
			dashboard.put("eventWiseRegistrations", eventStats);
			dashboard.put("totalEarnings", eventRepo.sumTotalEarnings());

			Map<String, Double> eventEarnings = new HashMap<>();
			List<Object[]> earningsData = eventRepo.getEventWiseEarnings();
			for (Object[] obj : earningsData) {
				eventEarnings.put((String) obj[0], (Double) obj[1]);
			}
			dashboard.put("eventWiseEarnings", eventEarnings);
		} else if (role.equals("STUDENT")) {
			Student student = studentRepo.findByEmail(email)
					.orElseThrow(() -> new RuntimeException("Student not found"));
			dashboard.put("studentName", student.getStudentName());
			dashboard.put("myRegistrations", registrationRepo.countByStudent(student));
			dashboard.put("approvedRegistrations", registrationRepo.countByStudentAndStatus(student, Status.APPROVED));
			dashboard.put("pendingRegistrations", registrationRepo.countByStudentAndStatus(student, Status.PENDING));
			dashboard.put("myEvents", registrationRepo.findStudentEvents(student));
		}

		return dashboard;
	}

	public String sendAdminOtp(AdminForgotPasswordDTO request) {

		Admin admin = adminRepo.findByEmailId(request.getEmailId())
				.orElseThrow(() -> new RuntimeException("Admin not found"));

		String otp = String.valueOf((int) (Math.random() * 900000) + 100000);

		admin.setOtp(otp);
		admin.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

		adminRepo.save(admin);

		emailService.sendOtpMail(admin.getEmailId(), admin.getName(), otp);

		return "OTP sent to admin email";
	}

	public String resetAdminPassword(AdminResetPasswordDTO request) {

		Admin admin = adminRepo.findByEmailId(request.getEmailId())
				.orElseThrow(() -> new RuntimeException("Admin not found"));

		if (admin.getOtp() == null || !admin.getOtp().equals(request.getOtp())) {
			throw new RuntimeException("Invalid OTP");
		}

		if (admin.getOtpExpiry().isBefore(LocalDateTime.now())) {
			throw new RuntimeException("OTP expired");
		}

		admin.setPassword(passwordEncoder.encode(request.getNewPassword()));

		admin.setOtp(null);
		admin.setOtpExpiry(null);

		adminRepo.save(admin);

		return "Admin password reset successful";
	}

	public String changePassword(Authentication authentication, AdminChangePasswordDTO request) {

		String email = authentication.getName();
		Admin admin = adminRepo.findByEmailId(email).orElseThrow(() -> new RuntimeException("Admin not found"));

		if (!passwordEncoder.matches(request.getOldPassword(), admin.getPassword())) {
			throw new RuntimeException("Old password is incorrect");
		}

		if (passwordEncoder.matches(request.getNewPassword(), admin.getPassword())) {
			throw new RuntimeException("New password cannot be same as old password"); 
		}

		admin.setPassword(passwordEncoder.encode(request.getNewPassword()));

		adminRepo.save(admin);

		return "Password updated successfully";
	}

}
