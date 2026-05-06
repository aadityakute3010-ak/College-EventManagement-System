package com.eventapp.Service;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.eventapp.Dao.EventRepository;
import com.eventapp.Dao.RegistrationRepository;
import com.eventapp.Dao.StudentRepository;
import com.eventapp.dto.ApproveRegistrationRequestDTO;
import com.eventapp.dto.RegistrationRequestDTO;
import com.eventapp.dto.RegistrationResponseDTO;
import com.eventapp.model.*;
import com.itextpdf.io.exceptions.IOException;
import jakarta.servlet.http.HttpServletResponse;
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
                pdf,
                event.getVenue(),
                event.getLocationLink()
        );
        

        return pdf;
    } 
    
    public void exportApprovedRegistrationsExcel(HttpServletResponse response) throws IOException, java.io.IOException {

        
        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        );
        response.setHeader("Content-Disposition",
                "attachment; filename=approved_registrations.xlsx");

        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Approved Registrations");

       
        XSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());

        XSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER); 
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN); 

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        
        int rowNum = 0;

        Row header = sheet.createRow(rowNum++);

        String[] columns = {
                "Category",
                "Event Name",
                "Student Name",
                "Email",
                "Registration Date"
        };

        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i); 
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle); 
        } 

        
        List<Registration> registrations = registrationRepo.findAllRegistrations();

        List<Registration> approved = registrations.stream()
                .filter(r -> r.getStatus() == Status.APPROVED)
                .toList();

        
        Map<EventCategory, List<Registration>> categoryMap =
                approved.stream()
                        .collect(Collectors.groupingBy(r -> r.getEvent().getCategory()));

        for (EventCategory category : EventCategory.values()) {

            List<Registration> categoryList = categoryMap.get(category);

            if (categoryList == null || categoryList.isEmpty()) continue;

            
            Map<String, List<Registration>> eventMap =
                    categoryList.stream()
                            .collect(Collectors.groupingBy(r -> r.getEvent().getEventName()));

            for (Map.Entry<String, List<Registration>> entry : eventMap.entrySet()) {

                String eventName = entry.getKey();

                for (Registration r : entry.getValue()) {

                    Row row = sheet.createRow(rowNum++);

                    row.createCell(0).setCellValue(category.toString());
                    row.createCell(1).setCellValue(eventName);
                    row.createCell(2).setCellValue(r.getStudent().getStudentName());
                    row.createCell(3).setCellValue(r.getStudent().getEmail());

                    String date = (r.getRegisteredAt() != null) 
                            ? r.getRegisteredAt().toString()
                            : "N/A";

                    row.createCell(4).setCellValue(date);

                    
                    for (int i = 0; i < 5; i++) {
                        row.getCell(i).setCellStyle(cellStyle);
                    }
                }
            }
        }

       
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        
        workbook.write(response.getOutputStream());
        workbook.close();
    } 
        
    
}