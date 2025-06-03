package com.example.BusBookingApplication.Service;



import com.example.BusBookingApplication.DTO.BookingDTO;
import com.example.BusBookingApplication.Entity.Booking;

import java.util.List;

public interface BookingService {
    Booking createBooking(BookingDTO bookingDTO);
    List<Booking> getBookingsByUserEmail(String email);

    List<String> getBookedSeats(Long busScheduleId);
}

