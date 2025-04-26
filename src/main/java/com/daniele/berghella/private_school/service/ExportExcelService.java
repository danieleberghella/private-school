package com.daniele.berghella.private_school.service;

import com.daniele.berghella.private_school.model.Course;
import com.daniele.berghella.private_school.model.Subject;
import lombok.extern.java.Log;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Log
@Service
public class ExportExcelService {

    @Autowired
    private CourseService courseService;

    public void generateCourseAndSubjectExcel(String filename) throws IOException {
        log.info(() -> "Generating Excel file for courses and subjects: " + filename);

        List<Course> courses = courseService.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Courses and Subjects");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Course ID");
        headerRow.createCell(1).setCellValue("Course Name");
        headerRow.createCell(2).setCellValue("Subject ID");
        headerRow.createCell(3).setCellValue("Subject Name");

        int rowNum = 1;
        for (Course course : courses) {
            for (Subject subject : course.getSubjects()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(course.getId());
                row.createCell(1).setCellValue(course.getTitle());
                row.createCell(2).setCellValue(subject.getId());
                row.createCell(3).setCellValue(subject.getName());
            }
        }

        try (FileOutputStream fileOut = new FileOutputStream(filename)) {
            workbook.write(fileOut);
            log.info(() -> "Excel file generated successfully: " + filename);
        } catch (IOException e) {
            log.severe(() -> "Failed to generate Excel file: " + e.getMessage());
            throw e;
        } finally {
            workbook.close();
        }
    }
}
