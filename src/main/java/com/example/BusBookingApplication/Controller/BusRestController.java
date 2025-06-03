package com.example.BusBookingApplication.Controller;

import com.example.BusBookingApplication.DTO.BusScheduleDTO;
import com.example.BusBookingApplication.Service.BusScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/buses")
public class BusRestController {

    private final BusScheduleService busScheduleService;

    @Autowired
    public BusRestController(BusScheduleService busScheduleService) {
        this.busScheduleService = busScheduleService;
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchSchedules(@RequestParam(required = false) String source,
                                             @RequestParam(required = false) String destination,
                                             @RequestParam(required = false) String date) {
        if (source == null || destination == null || date == null) {
            return ResponseEntity.badRequest().body("Source, destination, and date are required");
        }

        try {
            LocalDate travelDate = LocalDate.parse(date);
            List<BusScheduleDTO> schedules = busScheduleService.searchSchedulesDate(source, destination, travelDate);
            return ResponseEntity.ok(schedules);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format. Expected format: YYYY-MM-DD");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> viewSchedule(@PathVariable Long id) {
        BusScheduleDTO schedule = busScheduleService.getScheduleById(id);
        if (schedule != null) {
            return ResponseEntity.ok(schedule);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
