package com.daniele.berghella.private_school.service;

import com.daniele.berghella.private_school.dto.StudentPdfDTO;
import com.daniele.berghella.private_school.model.Enrollment;
import com.daniele.berghella.private_school.model.Student;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Log
@Service
public class ExportPdfService {

    @Autowired
    private StudentService studentService;

    public void generateStudentProfilePdf(Long studentId, String filename) throws DocumentException, IOException {
        log.info(() -> "Generating PDF profile for student ID: " + studentId);

        Student student = studentService.findById(studentId).orElse(null);

        if (student == null) {
            log.warning(() -> "Student not found with id: " + studentId);
            throw new IllegalArgumentException("Student not found with id: " + studentId);
        }

        List<String> courseNames = student.getEnrollments().stream()
                .map(Enrollment::getCourse)
                .map(course -> course.getTitle())
                .collect(Collectors.toList());

        StudentPdfDTO dto = new StudentPdfDTO(
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                courseNames
        );

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();

            document.add(new Paragraph("Student Profile"));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("First Name: " + dto.getFirstName()));
            document.add(new Paragraph("Last Name: " + dto.getLastName()));
            document.add(new Paragraph("Email: " + dto.getEmail()));
            document.add(new Paragraph("Courses Enrolled: " + dto.getCourseNames().size()));
            document.add(new Paragraph("Course List:"));
            for (String courseName : dto.getCourseNames()) {
                document.add(new Paragraph(" - " + courseName));
            }

            log.info(() -> "PDF file generated successfully: " + filename);
        } catch (DocumentException | IOException e) {
            log.severe(() -> "Failed to generate PDF: " + e.getMessage());
            throw e;
        } finally {
            document.close();
        }
    }
}
