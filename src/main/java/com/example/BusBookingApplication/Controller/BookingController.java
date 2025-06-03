package com.example.BusBookingApplication.Controller;

import com.example.BusBookingApplication.DTO.BookingDTO;
import com.example.BusBookingApplication.DTO.BusScheduleDTO;
import com.example.BusBookingApplication.DTO.PassengerDTO;
import com.example.BusBookingApplication.Entity.Booking;
import com.example.BusBookingApplication.Service.BookingService;
import com.example.BusBookingApplication.Service.BusScheduleService;
import com.example.BusBookingApplication.Service.impl.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final BusScheduleService busScheduleService;
    @Autowired
    private EmailService emailService;

    @Autowired
    public BookingController(BookingService bookingService, BusScheduleService busScheduleService) {
        this.bookingService = bookingService;
        this.busScheduleService = busScheduleService;
    }

    @GetMapping("/form")
    public String showBookingForm(@RequestParam Long busScheduleId, Model model) {
        BusScheduleDTO schedule = busScheduleService.getScheduleById(busScheduleId);

        int totalSeats = schedule.getTotalSeats();  // add this field in DTO
        List<String> bookedSeats = bookingService.getBookedSeats(busScheduleId);

        model.addAttribute("schedule", schedule);
        model.addAttribute("totalSeats", totalSeats);
        model.addAttribute("bookedSeats", bookedSeats);

        return "select-seats"; //for seat selection
    }

    @GetMapping("/select-seat")
    public String selectSeats(@RequestParam Long busScheduleId, Model model) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setBusScheduleId(busScheduleId);

        // Fetch booked seats to disable them in UI
        List<String> bookedSeats = bookingService.getBookedSeats(busScheduleId);

        model.addAttribute("booking", bookingDTO);
        model.addAttribute("bookedSeats", bookedSeats);
        model.addAttribute("busScheduleId", busScheduleId);
        return "select-seat";
    }

    @PostMapping("/passenger-info")
    public String showPassengerInfoForm(@RequestParam Long busScheduleId,
                                        @RequestParam String selectedSeats,
                                        Model model) {

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

        model.addAttribute("booking", bookingDTO);
        return "passenger-info";  // for passenger info input
    }

    @PostMapping("/submit")
    public String submitBooking(@ModelAttribute("booking") BookingDTO bookingDTO,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model) {
        bookingDTO.setUserEmail(userDetails.getUsername());
        Booking booking = bookingService.createBooking(bookingDTO);
        model.addAttribute("booking", booking);

        // for email model
        Map<String, Object> emailModel = new HashMap<>();
        emailModel.put("name", userDetails.getUsername());
        emailModel.put("busName", booking.getBusSchedule().getBus().getBusName());
        emailModel.put("source", booking.getBusSchedule().getSource());
        emailModel.put("destination", booking.getBusSchedule().getDestination());
        emailModel.put("departure", booking.getBusSchedule().getDepartureTime().toString());
        emailModel.put("price", booking.getBusSchedule().getPrice());
        emailModel.put("passengers", booking.getPassengers());

        emailService.sendBookingConfirmationEmail(userDetails.getUsername(), emailModel);

        return "booking-success";
    }
    // Booking history
    @GetMapping("/history")
    public String viewHistory(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<Booking> bookings = bookingService.getBookingsByUserEmail(userDetails.getUsername());
        model.addAttribute("bookings", bookings);
        return "booking-history";
    }
}
