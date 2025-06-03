package com.example.BusBookingApplication.Controller;

import com.example.BusBookingApplication.DTO.BusDTO;
import com.example.BusBookingApplication.DTO.BusScheduleDTO;
import com.example.BusBookingApplication.Entity.Bus;
import com.example.BusBookingApplication.Service.BusScheduleService;
import com.example.BusBookingApplication.Service.BusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminBusController {

    private final BusService busService;
    private final BusScheduleService scheduleService;

    @Autowired
    public AdminBusController(BusService busService, BusScheduleService scheduleService) {
        this.busService = busService;
        this.scheduleService = scheduleService;
    }

    @GetMapping("/dashboard")
    public String showAdminDashboard() {
        return "admin/dashboard"; // refers admin/dashboard.html
    }
    @GetMapping("/bus/add")
    public String showAddBusForm(Model model) {
        model.addAttribute("bus", new BusDTO());
        return "admin/add-bus";
    }

    @PostMapping("/bus/add")
    public String processAddBus(@ModelAttribute BusDTO busDTO) {
        busService.saveBus(busDTO);
        return "redirect:/admin/bus/add?success";
    }

    @GetMapping("/schedule/add")
    public String showAddScheduleForm(Model model) {
        model.addAttribute("schedule", new BusScheduleDTO());
        List<Bus> buses = busService.getAllBuses();
        model.addAttribute("buses", buses);
        return "admin/add-schedule";
    }

    @PostMapping("/schedule/add")
    public String processAddSchedule(@Valid @ModelAttribute("schedule") BusScheduleDTO scheduleDTO,
                                     BindingResult bindingResult,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("buses", busService.getAllBuses()); // repopulate dropdown
            return "admin/add-schedule";
        }

        Long busId = scheduleDTO.getBusId();
        if (busId == null) {
            bindingResult.rejectValue("busId", "error.schedule", "Bus must be selected");
            model.addAttribute("buses", busService.getAllBuses());
            return "admin/add-schedule";
        }

        Bus bus = busService.getBusEntityById(busId);
        if (bus == null) {
            bindingResult.rejectValue("busId", "error.schedule", "Selected bus not found");
            model.addAttribute("buses", busService.getAllBuses());
            return "admin/add-schedule";
        }

        // Call service to save schedule, passing both DTO and Bus entity
        scheduleService.addSchedule(scheduleDTO, bus);

        return "redirect:/admin/schedule/add?success";
    }


}
