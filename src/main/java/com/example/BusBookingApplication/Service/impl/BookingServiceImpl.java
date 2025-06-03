package com.example.BusBookingApplication.Service.impl;

import com.example.BusBookingApplication.DTO.BookingDTO;
import com.example.BusBookingApplication.DTO.PassengerDTO;
import com.example.BusBookingApplication.Entity.*;
import com.example.BusBookingApplication.Repository.BookingRepository;
import com.example.BusBookingApplication.Repository.BusScheduleRepository;
import com.example.BusBookingApplication.Repository.PassengerRepository;
import com.example.BusBookingApplication.Service.BookingService;
import com.example.BusBookingApplication.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final BusScheduleRepository busScheduleRepository;
    private final UserService userService;

    @Autowired
    public BookingServiceImpl(
            BookingRepository bookingRepository,
            PassengerRepository passengerRepository,
            BusScheduleRepository busScheduleRepository,
            UserService userService
    ) {
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.busScheduleRepository = busScheduleRepository;
        this.userService = userService;
    }

    @Override
    public Booking createBooking(BookingDTO bookingDTO) {
        User user = userService.getUserByEmail(bookingDTO.getUserEmail());

        BusSchedule busSchedule = busScheduleRepository.findById(bookingDTO.getBusScheduleId())
                .orElseThrow(() -> new RuntimeException("Bus schedule not found"));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setBusSchedule(busSchedule); // set BusSchedule
        booking.setBookingTime(LocalDateTime.now());
        booking.setNumberOfSeats(bookingDTO.getPassengers().size());
        booking.setTotalFare(busSchedule.getPrice() * booking.getNumberOfSeats());

        List<Passenger> passengers = new ArrayList<>();
        for (PassengerDTO dto : bookingDTO.getPassengers()) {
            Passenger passenger = new Passenger();
            passenger.setName(dto.getName());
            passenger.setAge(dto.getAge());
            passenger.setGender(dto.getGender());
            passenger.setSeatNumber(dto.getSeatNumber());
            passenger.setBooking(booking);
            passengers.add(passenger);
        }

        booking.setPassengers(passengers);

        return bookingRepository.save(booking);
    }


    @Override
    public List<Booking> getBookingsByUserEmail(String email) {
        User user = userService.getUserByEmail(email);
        return bookingRepository.findByUser(user);
    }

    @Override
    public List<String> getBookedSeats(Long scheduleId) {
        List<String> bookedSeats = bookingRepository.findSeatsByScheduleId(scheduleId);
        return bookedSeats;
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
    }

}
