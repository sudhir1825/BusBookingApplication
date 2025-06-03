package com.example.BusBookingApplication.Controller;

import com.example.BusBookingApplication.DTO.BusScheduleDTO;
import com.example.BusBookingApplication.Service.BusScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@RequestMapping("/buses")
public class BusController {

    private final BusScheduleService busScheduleService;

    @Autowired
    public BusController(BusScheduleService busScheduleService) {
        this.busScheduleService = busScheduleService;
    }

    @GetMapping("/search")
    public String searchSchedules(@RequestParam(required = false) String source,
                                  @RequestParam(required = false) String destination,
                                  @RequestParam(required = false) String date,
                                  Model model) {
        if (source != null && destination != null && date != null) {
            try {
                LocalDate travelDate = LocalDate.parse(date);
                List<BusScheduleDTO> schedules = busScheduleService.searchSchedulesDate(source, destination, travelDate);
                model.addAttribute("schedules", schedules);
            } catch (DateTimeParseException e) {
                model.addAttribute("dateError", "Invalid date format");
            }
        }
        return "search";
    }

    @GetMapping("{id}")
    public String viewSchedule(@PathVariable Long id, Model model) {
        BusScheduleDTO schedule = busScheduleService.getScheduleById(id);
        model.addAttribute("schedule", schedule);
        return "book-bus";
    }
}
