package com.daniele.berghella.private_school;

import com.daniele.berghella.private_school.dto.*;
import com.daniele.berghella.private_school.model.*;
import com.daniele.berghella.private_school.repository.*;
import com.daniele.berghella.private_school.util.JwtTokenUtil;
import com.daniele.berghella.private_school.service.*;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class AuthServiceTest {

	@InjectMocks
	private AuthService authService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private JwtTokenUtil jwtTokenUtil;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAuthenticateUser_success() {
		String rawPassword = "password123";
		String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

		Users user = new Users();
		user.setUsername("testuser");
		user.setPassword(hashedPassword);

		when(userRepository.findByUsername("testuser")).thenReturn(user);
		when(jwtTokenUtil.generateToken(user)).thenReturn("mocked-jwt-token");

		LoginRequestDTO loginRequest = new LoginRequestDTO("testuser", rawPassword);
		LoginResponseDTO response = authService.authenticateUser(loginRequest);

		assertNotNull(response);
		assertEquals("mocked-jwt-token", response.getToken());
	}

	@Test
	void testAuthenticateUser_invalidPassword() {
		Users user = new Users();
		user.setUsername("testuser");
		user.setPassword(BCrypt.hashpw("correctPassword", BCrypt.gensalt()));

		when(userRepository.findByUsername("testuser")).thenReturn(user);

		LoginRequestDTO loginRequest = new LoginRequestDTO("testuser", "wrongPassword");

		assertThrows(RuntimeException.class, () -> {
			authService.authenticateUser(loginRequest);
		});
	}

	@Test
	void testRegisterUser_success() {
		SignupRequestDTO request = new SignupRequestDTO("newuser", "newpass");

		Users savedUser = new Users();
		savedUser.setUsername("newuser");
		savedUser.setRole("USER");

		when(userRepository.findByUsername("newuser")).thenReturn(null);
		when(userRepository.save(any(Users.class))).thenReturn(savedUser);

		SignUpResponseDTO response = authService.registerUser(request);

		assertNotNull(response);
		assertEquals("newuser", response.getUsername());
		assertEquals("USER", response.getRole());
		assertNull(response.getPassword());

		verify(userRepository, times(1)).save(any(Users.class));
	}

	@Test
	void testRegisterUser_alreadyExists() {
		Users existingUser = new Users();
		existingUser.setUsername("existinguser");

		when(userRepository.findByUsername("existinguser")).thenReturn(existingUser);

		SignupRequestDTO request = new SignupRequestDTO("existinguser", "pass");

		assertThrows(RuntimeException.class, () -> {
			authService.registerUser(request);
		});
	}
}

class ClassroomServiceTest {

	@InjectMocks
	private ClassroomService classroomService;

	@Mock
	private ClassroomRepository classroomRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindAll() {
		List<Classroom> mockClassrooms = Arrays.asList(
				new Classroom("Aula 101", 30),
				new Classroom("Aula 102", 25)
		);

		when(classroomRepository.findAll()).thenReturn(mockClassrooms);

		List<Classroom> result = classroomService.findAll();

		assertEquals(2, result.size());
		verify(classroomRepository, times(1)).findAll();
	}

	@Test
	void testFindById_found() {
		Classroom classroom = new Classroom("Classroom 101", 30);
		when(classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));

		Optional<Classroom> result = classroomService.findById(1L);

		assertTrue(result.isPresent());
		assertEquals("Classroom 101", result.get().getName());
		verify(classroomRepository, times(1)).findById(1L);
	}

	@Test
	void testFindById_notFound() {
		when(classroomRepository.findById(999L)).thenReturn(Optional.empty());

		Optional<Classroom> result = classroomService.findById(999L);

		assertFalse(result.isPresent());
	}

	@Test
	void testSave() {
		Classroom classroom = new Classroom("Aula 103", 35);

		when(classroomRepository.save(classroom)).thenReturn(classroom);

		Classroom result = classroomService.save(classroom);

		assertNotNull(result);
		assertEquals("Aula 103", result.getName());
		assertEquals(35, result.getCapacity());
		verify(classroomRepository, times(1)).save(classroom);
	}

	@Test
	void testDeleteById() {
		Long classroomId = 1L;

		classroomService.deleteById(classroomId);

		verify(classroomRepository, times(1)).deleteById(classroomId);
	}
}

@ExtendWith(MockitoExtension.class)
class ExportCsvServiceTest {

	@Mock private StudentService studentService;
	@Mock private TeacherService teacherService;
	@Mock private EnrollmentService enrollmentService;
	@Mock private CsvMapper csvMapper;
	@Mock private ModelMapper modelMapper;
	@Mock private ObjectWriter objectWriter;

	@InjectMocks private ExportCsvService exportCsvService;

	@TempDir Path tempDir;
	private File tempFile;

	@BeforeEach
	void setUp() {
		tempFile = tempDir.resolve("test.csv").toFile();
	}

	@Test
	void testExportStudentsToCsv() throws Exception {
		Student student = new Student();
		StudentDTO dto = new StudentDTO();

		when(studentService.findAll()).thenReturn(List.of(student));
		when(modelMapper.map(student, StudentDTO.class)).thenReturn(dto);

		CsvSchema schema = CsvSchema.emptySchema().withHeader();
		when(csvMapper.schemaFor(StudentDTO.class)).thenReturn(schema);
		when(csvMapper.writerFor(List.class)).thenReturn(objectWriter);
		when(objectWriter.with(schema)).thenReturn(objectWriter);

		exportCsvService.exportStudentsToCsv(tempFile.getAbsolutePath());

		verify(studentService).findAll();
		verify(csvMapper).writerFor(List.class);
		verify(objectWriter).writeValue(eq(tempFile), any());
	}

	@Test
	void testExportTeachersToCsv() throws Exception {
		Teacher teacher = new Teacher();
		TeacherDTO dto = new TeacherDTO();

		when(teacherService.findAll()).thenReturn(List.of(teacher));
		when(modelMapper.map(teacher, TeacherDTO.class)).thenReturn(dto);

		CsvSchema schema = CsvSchema.emptySchema().withHeader();
		when(csvMapper.schemaFor(TeacherDTO.class)).thenReturn(schema);
		when(csvMapper.writerFor(List.class)).thenReturn(objectWriter);
		when(objectWriter.with(schema)).thenReturn(objectWriter);

		exportCsvService.exportTeachersToCsv(tempFile.getAbsolutePath());

		verify(teacherService).findAll();
		verify(csvMapper).writerFor(List.class);
		verify(objectWriter).writeValue(eq(tempFile), any());
	}

	@Test
	void testExportEnrollmentsToCsv() throws Exception {
		Enrollment enrollment = new Enrollment();
		EnrollmentCsvDTO dto = new EnrollmentCsvDTO();

		when(enrollmentService.findAll()).thenReturn(List.of(enrollment));
		when(modelMapper.map(enrollment, EnrollmentCsvDTO.class)).thenReturn(dto);

		CsvSchema schema = CsvSchema.emptySchema().withHeader();
		when(csvMapper.schemaFor(EnrollmentCsvDTO.class)).thenReturn(schema);
		when(csvMapper.writerFor(List.class)).thenReturn(objectWriter);
		when(objectWriter.with(schema)).thenReturn(objectWriter);

		exportCsvService.exportEnrollmentsToCsv(tempFile.getAbsolutePath());

		verify(enrollmentService).findAll();
		verify(csvMapper).writerFor(List.class);
		verify(objectWriter).writeValue(eq(tempFile), any());
	}
}
