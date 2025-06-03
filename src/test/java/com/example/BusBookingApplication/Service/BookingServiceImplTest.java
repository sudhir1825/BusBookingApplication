package com.example.BusBookingApplication.Service;

import com.example.BusBookingApplication.DTO.BookingDTO;
import com.example.BusBookingApplication.DTO.PassengerDTO;
import com.example.BusBookingApplication.Entity.*;
import com.example.BusBookingApplication.Repository.BookingRepository;
import com.example.BusBookingApplication.Repository.BusScheduleRepository;
import com.example.BusBookingApplication.Repository.PassengerRepository;
import com.example.BusBookingApplication.Service.UserService;
import com.example.BusBookingApplication.Service.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private BusScheduleRepository busScheduleRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBooking_ShouldSaveAndReturnBooking() {
        // Arrange
        String userEmail = "test@example.com";
        Long busScheduleId = 1L;

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setUserEmail(userEmail);
        bookingDTO.setBusScheduleId(busScheduleId);

        PassengerDTO passenger1 = new PassengerDTO("Alice", 25, "Female", "A1");
        PassengerDTO passenger2 = new PassengerDTO("Bob", 30, "Male", "A2");
        bookingDTO.setPassengers(List.of(passenger1, passenger2));

        User mockUser = new User();
        mockUser.setEmail(userEmail);

        BusSchedule busSchedule = new BusSchedule();
        busSchedule.setId(busScheduleId);
        busSchedule.setPrice(100.0);

        Booking savedBooking = new Booking();
        savedBooking.setId(1L);
        savedBooking.setUser(mockUser);
        savedBooking.setBusSchedule(busSchedule);
        savedBooking.setNumberOfSeats(2);
        savedBooking.setTotalFare(200.0);
        savedBooking.setBookingTime(LocalDateTime.now());

        when(userService.getUserByEmail(userEmail)).thenReturn(mockUser);
        when(busScheduleRepository.findById(busScheduleId)).thenReturn(Optional.of(busSchedule));
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

        // Act
        Booking result = bookingService.createBooking(bookingDTO);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getNumberOfSeats());
        assertEquals(200.0, result.getTotalFare());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void getBookingsByUserEmail_ShouldReturnList() {
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);

        List<Booking> mockBookings = List.of(new Booking(), new Booking());

        when(userService.getUserByEmail(email)).thenReturn(user);
        when(bookingRepository.findByUser(user)).thenReturn(mockBookings);

        List<Booking> result = bookingService.getBookingsByUserEmail(email);

        assertEquals(2, result.size());
    }

    @Test
    void getBookedSeats_ShouldReturnSeatList() {
        Long scheduleId = 1L;
        List<String> bookedSeats = List.of("1", "5", "10");

        when(bookingRepository.findSeatsByScheduleId(scheduleId)).thenReturn(bookedSeats);

        List<String> result = bookingService.getBookedSeats(scheduleId);

        assertEquals(bookedSeats, result);
    }

    @Test
    void getBookingById_ShouldReturnBooking() {
        Long bookingId = 10L;
        Booking booking = new Booking();
        booking.setId(bookingId);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        Booking result = bookingService.getBookingById(bookingId);

        assertEquals(bookingId, result.getId());
    }

    @Test
    void getBookingById_ShouldThrowException_WhenNotFound() {
        Long bookingId = 100L;
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            bookingService.getBookingById(bookingId);
        });

        assertTrue(ex.getMessage().contains("Booking not found with id"));
    }
}

