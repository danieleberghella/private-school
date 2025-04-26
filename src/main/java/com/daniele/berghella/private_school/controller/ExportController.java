package com.daniele.berghella.private_school.controller;

import com.daniele.berghella.private_school.service.ExportCsvService;
import com.daniele.berghella.private_school.service.ExportPdfService;
import com.daniele.berghella.private_school.service.ExportExcelService;
import com.daniele.berghella.private_school.service.StudentService;
import com.daniele.berghella.private_school.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/export")
public class ExportController {

    @Autowired
    private ExportCsvService exportCsvService;

    @Autowired
    private ExportPdfService exportPdfService;

    @Autowired
    private ExportExcelService exportExcelService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/students/csv")
    @Operation(summary = "Export CSV", description = "Export CSV file with all students")
    public String exportStudentsCsv() throws IOException {
        exportCsvService.exportStudentsToCsv("students.csv");
        return "Students CSV file generated.";
    }

    @GetMapping("/teachers/csv")
    @Operation(summary = "Export CSV", description = "Export CSV file with all teachers")
    public String exportTeachersCsv() throws IOException {
        exportCsvService.exportTeachersToCsv("teachers.csv");
        return "Teachers CSV file generated.";
    }

    @GetMapping("/enrollments/csv")
    @Operation(summary = "Export CSV", description = "Export CSV file with all enrollments")
    public String exportEnrollmentsCsv() throws IOException {
        exportCsvService.exportEnrollmentsToCsv("enrollments.csv");
        return "Enrollments CSV file generated.";
    }

    @GetMapping("/student/{id}/pdf")
    @Operation(summary = "Export PDF", description = "Export PDF file with a student details")
    public String generateStudentProfilePdf(@PathVariable Long id) throws IOException {
            exportPdfService.generateStudentProfilePdf( id, "StudentId_" + id + "_profile.pdf");
            return "Student profile PDF generated.";
    }

    @GetMapping("/courses/excel")
    @Operation(summary = "Export Excel", description = "Export xlsx file with all courses and subjects")
    public String generateCoursesExcel() throws IOException {
        exportExcelService.generateCourseAndSubjectExcel("courses_and_subjects.xlsx");
        return "Courses and subjects Excel file generated.";
    }
}
