# Private School Management System

## Objective
The goal of this project is to develop a RESTful backend application in Java using Spring Boot for managing a private school offering post-graduate technical courses such as programming, graphic design, and networking. The system will handle students, teachers, courses, enrollments, and final evaluations. The application is backend-only and will be fully tested via Postman, with comprehensive Swagger/OpenAPI documentation.

## Technologies
- **Java 21**
- **Spring Boot** (Spring Boot Starter Data JPA, Spring Boot Starter Web, Spring Boot DevTools)
- **PostgreSQL** (Database)
- **Lombok** (For boilerplate code reduction)
- **ModelMapper** (For DTO mapping)
- **OpenAPI / Swagger** (API documentation)
- **OpenPDF** (For PDF generation)
- **Apache POI** (For Excel export)
- **Jackson CSV** (For CSV export)
- **JUnit & Mockito** (For unit testing and mocking dependencies)

## Architecture
- **Layered architecture** with a separation of concerns:
    - **Controller layer**: Handles HTTP requests and responses.
    - **Service layer**: Contains business logic and interacts with the repository layer.
    - **Repository layer**: Uses Spring Data JPA to interact with the PostgreSQL database.
- **Relational Database Model** with entities such as `User`, `Teacher`, `Student`, `Course`, `Enrollment`, and `Evaluation`.
- **JWT-based Authentication** for secure API access.
- **Swagger/OpenAPI** for API documentation.

## Security and Authentication
- JWT (JSON Web Token) is used for secure authentication and authorization.

## Key Features
- CRUD operations for managing entities: Students, Teachers, Courses, Enrollments, and Evaluations.
- **Many-to-Many** relationship between `Students` and `Schedules`.
- **Dataset pre-loaded** with realistic data at application startup.
- **CSV Export**: Export of student, teacher, and enrollment data to CSV format.
- **PDF Generation**: Generate a PDF profile for individual students.
- **Excel Export**: Export a list of study paths and corresponding exams to an Excel file.
- **User Registration**: API endpoint to register a new user in the system.

## Additional Features
- **Swagger/OpenAPI Documentation** for easy API exploration and testing.
- **Logging**: Uses SLF4J and Logback for logging application events.
- **Unit Testing**: Includes comprehensive unit tests using JUnit and Mockito for mocking dependencies.

## Testing
- **Unit Tests**: Each service and controller is tested using JUnit.
- **Mockito**: Used for mocking external dependencies such as repositories and services.
- **Postman**: Used for manual testing of RESTful endpoints.

## Running the Project
1. **Clone the repository**:
   ```bash
   git clone https://github.com/danieleberghella/private-school
   ```

2. **Set up the PostgreSQL database**:
    - Create a PostgreSQL database named `private-school`.
    - Configure the database connection in `application.properties`:
      ```properties
      spring.datasource.url=jdbc:postgresql://localhost:5432/private-school
      spring.datasource.driverClassName=org.postgresql.Driver
      spring.datasource.username=your_db_username
      spring.datasource.password=your_db_password
      ```

4. **Access the Swagger UI** at `http://localhost:8000/swagger-ui.html` to explore and test the API endpoints.

## Initial Dataset
- At application startup, the system is pre-loaded with realistic data, including students, teachers, courses, and enrollments.
- The dataset is used for testing and demonstration purposes.

## Design Patterns Used

- **DTO (Data Transfer Object)**: Used to transfer data between layers, minimizing the exposure of entity objects.
- **MVC (Model-View-Controller)**:
    - **Model**: Represents the application's data and business logic. In this project, the model consists of entities such as `Student`, `Teacher`, `Course`, etc. These entities are mapped to database tables using JPA.
    - **View**: Although the application is a backend service without a user interface, the concept of "view" can be represented by the data returned by the API endpoints (i.e., JSON responses). The data is structured according to the DTOs.
    - **Controller**: Manages HTTP requests and responses. Each controller (e.g., `StudentController`, `TeacherController`) handles specific CRUD operations and interacts with the service layer to process data.
    - **Service Layer**: Encapsulates business logic and handles interactions with the data layer.
    - **Repository**: Interfaces between the service layer and the database. The repositories handle CRUD operations on the entities and use Spring Data JPA to interact with the PostgreSQL database.

## Final Notes
- The project is designed to be highly extensible, allowing for additional features to be added easily, such as advanced reporting, user roles, and more detailed evaluation systems.
- Ensure that your PostgreSQL database is properly configured before running the application.
- All endpoints are documented using Swagger/OpenAPI, making it easy to test them using Postman or other API testing tools.