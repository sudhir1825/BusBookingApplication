# 🚌 Bus Booking Application

A full-stack web application built using **Spring Boot**, **Spring Security**, **Thymeleaf**, and **MySQL** that enables users to search for buses, book tickets, view booking history, and receive email confirmation upon successful booking.

---

## 🚀 Tech Stack

- **Backend:** Java, Spring Boot, Spring Security
- **Frontend:** Thymeleaf, HTML, Bootstrap
- **Database:** MySQL
- **Mail Service:** JavaMailSender
- **Authentication:** Spring Security with CustomUserDetailsService
- **Build Tool:** Maven

---

## 📁 Project Structure

```bash
BusBookingApplication/
├── docs/
│ └── BusAppSwagger.json
├── src/
│ ├── main/
│ │ ├── java/com.example.BusBookingApplication/
│ │ │ ├── Config/
│ │ │ │ └── SecurityConfig.java
│ │ │ ├── Controller/
│ │ │ │ ├── AdminBusController.java
│ │ │ │ ├── AdminBusRestController.java
│ │ │ │ ├── AuthController.java
│ │ │ │ ├── AuthRestController.java
│ │ │ │ ├── BookingController.java
│ │ │ │ ├── BookingRestController.java
│ │ │ │ ├── BusController.java
│ │ │ │ ├── BusRestController.java
│ │ │ │ └── UserController.java
│ │ │ ├── DTO/
│ │ │ │ ├── BookingDTO.java
│ │ │ │ ├── BusDTO.java
│ │ │ │ ├── BusScheduleDTO.java
│ │ │ │ ├── PassengerDTO.java
│ │ │ │ └── UserDTO.java
│ │ │ ├── Entity/
│ │ │ │ ├── Booking.java
│ │ │ │ ├── Bus.java
│ │ │ │ ├── BusSchedule.java
│ │ │ │ ├── Passenger.java
│ │ │ │ └── User.java
│ │ │ ├── Exception/
│ │ │ │ ├── GlobalExceptionHandler.java
│ │ │ │ ├── InvalidCredentialsException.java
│ │ │ │ └── UserAlreadyExistsException.java
│ │ │ ├── Repository/
│ │ │ │ ├── BookingRepository.java
│ │ │ │ ├── BusRepository.java
│ │ │ │ ├── BusScheduleRepository.java
│ │ │ │ ├── PassengerRepository.java
│ │ │ │ └── UserRepository.java
│ │ │ ├── Service/
│ │ │ │ ├── impl/
│ │ │ │ │ ├── BookingServiceImpl.java
│ │ │ │ │ ├── BusScheduleServiceImpl.java
│ │ │ │ │ ├── BusServiceImpl.java
│ │ │ │ │ ├── CustomUserDetailsService.java
│ │ │ │ │ ├── EmailService.java
│ │ │ │ │ └── UserServiceImpl.java
│ │ │ │ ├── BookingService.java
│ │ │ │ ├── BusScheduleService.java
│ │ │ │ ├── BusService.java
│ │ │ │ └── UserService.java
│ │ │ └── BusBookingApplication.java
│ │ ├── resources/
│ │ │ ├── static/
│ │ │ ├── templates/
│ │ │ │ ├── admin/
│ │ │ │ │ ├── add-bus.html
│ │ │ │ │ ├── add-schedule.html
│ │ │ │ │ └── dashboard.html
│ │ │ │ ├── email/
│ │ │ │ │ └── booking-confirmation.html
│ │ │ │ ├── fragments/
│ │ │ │ │ ├── booking-history.html
│ │ │ │ │ ├── booking-success.html
│ │ │ │ │ ├── home.html
│ │ │ │ │ ├── login.html
│ │ │ │ │ ├── passenger-info.html
│ │ │ │ │ ├── profile.html
│ │ │ │ │ ├── register.html
│ │ │ │ │ ├── search.html
│ │ │ │ │ ├── select-seat.html
│ │ │ │ │ └── view-buses.html
│ │ │ └── application.properties
│ └── test/
│ └── java/com.example.BusBookingApplication/
│ ├── Controller/
│ │ ├── AdminBusControllerTest.java
│ │ ├── AuthControllerTest.java
│ │ └── BusControllerTest.java
│ ├── Service/
│ │ ├── BookingServiceImplTest.java
│ │ ├── BusScheduleServiceImplTest.java
│ │ ├── BusServiceImplTest.java
│ │ └── UserServiceImplTest.java
│ └── BusBookingApplicationTests.java
├── pom.xml
```


---

## 📌 Features

- 🔐 User registration and login with Spring Security
- 🔍 Search buses by source, destination, and date
- 🎫 Seat selection and booking
- 📧 Email confirmation sent to user after successful booking
- 👤 Admin panel for adding buses and schedules

---

## 🛠️ How to Run the Project

### 1. Clone the repository:
```bash
git clone https://github.com/your-username/BusBookingApplication.git
cd BusBookingApplication
```

### 2. Configure the database
Make sure you have MySQL running and update the application.properties with your DB credentials:
```bash
spring.datasource.url=jdbc:mysql://localhost:3306/busbooking
spring.datasource.username=root
spring.datasource.password=yourpassword
```


### 3. Build the project using Maven:
```bash
./mvnw clean install
```


### 4. Run the application:
```bash
./mvnw spring-boot:run
```

### 5. Access in Browser:
```bash
http://localhost:8080
```

## ✅ Email Notification

Upon successful booking, the user will receive a booking confirmation email using the EmailService and Thymeleaf email template (booking-confirmation.html).


