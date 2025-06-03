package com.example.BusBookingApplication.Controller;

import com.example.BusBookingApplication.DTO.BookingDTO;
import com.example.BusBookingApplication.DTO.BusScheduleDTO;
import com.example.BusBookingApplication.DTO.PassengerDTO;
import com.example.BusBookingApplication.Entity.Booking;
import com.example.BusBookingApplication.Service.BookingService;
import com.example.BusBookingApplication.Service.BusScheduleService;
import com.example.BusBookingApplication.Service.impl.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingRestController {

    private final BookingService bookingService;
    private final BusScheduleService busScheduleService;
    private final EmailService emailService;

    @Autowired
    public BookingRestController(BookingService bookingService,
                                 BusScheduleService busScheduleService,
                                 EmailService emailService) {
        this.bookingService = bookingService;
        this.busScheduleService = busScheduleService;
        this.emailService = emailService;
    }

    @GetMapping("/form")
    public ResponseEntity<?> getBookingForm(@RequestParam Long busScheduleId) {
        BusScheduleDTO schedule = busScheduleService.getScheduleById(busScheduleId);
        Map<String, Object> response = new HashMap<>();
        response.put("schedule", schedule);
        response.put("totalSeats", schedule.getTotalSeats());
        response.put("bookedSeats", bookingService.getBookedSeats(busScheduleId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/booked-seats")
    public ResponseEntity<List<String>> getBookedSeats(@RequestParam Long busScheduleId) {
        List<String> bookedSeats = bookingService.getBookedSeats(busScheduleId);
        return ResponseEntity.ok(bookedSeats);
    }

    @PostMapping("/prepare-passenger-info")
    public ResponseEntity<?> preparePassengerInfo(@RequestParam Long busScheduleId,
                                                  @RequestParam String selectedSeats) {
        String[] seats = selectedSeats.split(",");
        List<PassengerDTO> passengers = new ArrayList<>();
        for (String seat : seats) {
            PassengerDTO p = new PassengerDTO();
            p.setSeatNumber(seat);
            passengers.add(p);
        }

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setBusScheduleId(busScheduleId);
        bookingDTO.setPassengers(passengers);

        return ResponseEntity.ok(bookingDTO);
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitBooking(@RequestBody BookingDTO bookingDTO,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        bookingDTO.setUserEmail(userDetails.getUsername());
        Booking booking = bookingService.createBooking(bookingDTO);

        Map<String, Object> emailModel = new HashMap<>();
        emailModel.put("name", userDetails.getUsername());
        emailModel.put("busName", booking.getBusSchedule().getBus().getBusName());
        emailModel.put("source", booking.getBusSchedule().getSource());
        emailModel.put("destination", booking.getBusSchedule().getDestination());
        emailModel.put("departure", booking.getBusSchedule().getDepartureTime().toString());
        emailModel.put("price", booking.getBusSchedule().getPrice());
        emailModel.put("passengers", booking.getPassengers());

        emailService.sendBookingConfirmationEmail(userDetails.getUsername(), emailModel);

        return ResponseEntity.ok(booking);
    }

    @GetMapping("/history")
    public ResponseEntity<List<Booking>> getBookingHistory(@AuthenticationPrincipal UserDetails userDetails) {
        List<Booking> bookings = bookingService.getBookingsByUserEmail(userDetails.getUsername());
        return ResponseEntity.ok(bookings);
    }
}
