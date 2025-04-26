package com.daniele.berghella.private_school.util;

import com.daniele.berghella.private_school.model.*;
import com.daniele.berghella.private_school.service.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Component
@Log
public class DataLoader implements CommandLineRunner {

    public static final String GENSALT = BCrypt.gensalt();

    @Autowired private UserService usersService;
    @Autowired private StudentService studentService;
    @Autowired private TeacherService teacherService;
    @Autowired private ClassroomService classroomService;
    @Autowired private CourseService courseService;
    @Autowired private SubjectService subjectService;
    @Autowired private EvaluationService evaluationService;
    @Autowired private EnrollmentService enrollmentService;
    @Autowired private ScheduleService scheduleService;

    @Override
    public void run(String... args) {

        Users u1 = usersService.save(Users.builder().username("student1").password(BCrypt.hashpw("pass", GENSALT)).role("STUDENT").build());
        Users u2 = usersService.save(Users.builder().username("student2").password(BCrypt.hashpw("pass", GENSALT)).role("STUDENT").build());
        Users u3 = usersService.save(Users.builder().username("teacher1").password(BCrypt.hashpw("pass", GENSALT)).role("TEACHER").build());
        Users u4 = usersService.save(Users.builder().username("teacher2").password(BCrypt.hashpw("pass", GENSALT)).role("TEACHER").build());


        Student s1 = studentService.save(Student.builder().firstName("Anna").lastName("Rossi").email("anna@example.com").user(u1).build());
        Student s2 = studentService.save(Student.builder().firstName("Luca").lastName("Bianchi").email("luca@example.com").user(u2).build());

        Teacher t1 = teacherService.save(Teacher.builder().firstName("Giovanni").lastName("Verdi").email("giovanni@example.com").user(u3).build());
        Teacher t2 = teacherService.save(Teacher.builder().firstName("Maria").lastName("Neri").email("maria@example.com").user(u4).build());

        Classroom c1 = classroomService.save(Classroom.builder().name("Room A").capacity(30).build());
        Classroom c2 = classroomService.save(Classroom.builder().name("Room B").capacity(25).build());

        Course course1 = courseService.save(Course.builder().title("Mathematics").description("Math course").teacher(t1).classroom(c1).build());
        Course course2 = courseService.save(Course.builder().title("Science").description("Science course").teacher(t2).classroom(c2).build());

        Subject sub1 = subjectService.save(Subject.builder().name("Algebra").course(course1).build());
        Subject sub2 = subjectService.save(Subject.builder().name("Physics").course(course2).build());

        enrollmentService.save(Enrollment.builder().student(s1).course(course1).enrollmentDate(LocalDate.now().toString()).build());
        enrollmentService.save(Enrollment.builder().student(s2).course(course2).enrollmentDate(LocalDate.now().toString()).build());

        evaluationService.save(Evaluation.builder().student(s1).subject(sub1).grade(8.5).comment("Good job").build());
        evaluationService.save(Evaluation.builder().student(s2).subject(sub2).grade(9.0).comment("Excellent").build());

        scheduleService.save(Schedule.builder()
                .course(course1)
                .subject(sub1)
                .classroom(c1)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .attendees(Set.of(s1))
                .build());

        scheduleService.save(Schedule.builder()
                .course(course2)
                .subject(sub2)
                .classroom(c2)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .attendees(Set.of(s2))
                .build());

        log.info("DataLoader completed.");
    }
}
