package com.eventapp.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;
import com.eventapp.model.Registration;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;

@Service
public class PdfService {

	public byte[] generateReceipt(Registration reg, String razorpayOrderId, LocalDateTime paymentTime, String invoiceNumber) {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf); 

            Table headerTable = new Table(1).useAllAvailableWidth();

            Cell headerCell = new Cell()
                    .add(new Paragraph("College EVENT MANAGEMENT Team\nPayment Receipt")
                    .setBold()
                    .setFontSize(16)
                    .setFontColor(ColorConstants.WHITE)
                    .setTextAlignment(TextAlignment.CENTER))
                    .setBackgroundColor(ColorConstants.BLUE)
                    .setPadding(10);

            headerTable.addCell(headerCell);
            document.add(headerTable);

            document.add(new Paragraph("\n\n"));

            String formattedTime = paymentTime.format(
            	    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
            	);

            Table topInfo = new Table(2).useAllAvailableWidth();

            topInfo.addCell(new Cell()
                    .add(new Paragraph("Invoice No: " + invoiceNumber).setBold())
                    .setBorder(null));

            topInfo.addCell(new Cell()
                    .add(new Paragraph("Generated On: " + formattedTime)) 
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBorder(null));

            document.add(topInfo);

            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Student Details").setBold());

            Table studentTable = new Table(2).useAllAvailableWidth(); 

            studentTable.addCell("Name");
            studentTable.addCell(reg.getStudent().getStudentName());

            studentTable.addCell("Email");
            studentTable.addCell(reg.getStudent().getEmail());

            document.add(studentTable);
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Event Details").setBold());

            Table eventTable = new Table(2).useAllAvailableWidth();

            eventTable.addCell("Event Name");
            eventTable.addCell(reg.getEvent().getEventName());

            eventTable.addCell("Event Date");
            eventTable.addCell(reg.getEvent().getEventDate().toString());

            document.add(eventTable);
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Payment Details").setBold());

            Table paymentTable = new Table(2).useAllAvailableWidth();

            paymentTable.addCell("Registration ID");
            paymentTable.addCell(String.valueOf(reg.getId()));

            paymentTable.addCell("Payment ID");
            paymentTable.addCell(reg.getPaymentId());

            paymentTable.addCell("Razorpay Order ID");
            paymentTable.addCell(razorpayOrderId);

            paymentTable.addCell("Amount Paid");
            paymentTable.addCell("₹" + reg.getEvent().getRegistrationFees());

            paymentTable.addCell("Payment Time");
            paymentTable.addCell(paymentTime.toString());

            paymentTable.addCell("Status");
            paymentTable.addCell("SUCCESS");

            document.add(paymentTable);
            document.add(new Paragraph("\n"));

            Paragraph footer = new Paragraph("Thank you for your registration!")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.GRAY); 

            document.add(footer);
            
            document.add(new Paragraph("\n"));

            Paragraph contactHeader = new Paragraph("Contact Us")
                    .setBold()
                    .setFontSize(12);

            Table contactTable = new Table(2).useAllAvailableWidth();

            contactTable.addCell(new Cell().add(new Paragraph("Email").setBold())); 
            contactTable.addCell("support@eventapp.com");

            contactTable.addCell(new Cell().add(new Paragraph("Phone").setBold()));
            contactTable.addCell("+91 9876543210");

            document.add(contactHeader);
            document.add(contactTable);

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF");
        }
    }
	
}
