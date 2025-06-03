package com.example.BusBookingApplication.Controller;

import com.example.BusBookingApplication.DTO.BusDTO;
import com.example.BusBookingApplication.DTO.BusScheduleDTO;
import com.example.BusBookingApplication.Entity.Bus;
import com.example.BusBookingApplication.Service.BusScheduleService;
import com.example.BusBookingApplication.Service.BusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminBusRestController {

    private final BusService busService;
    private final BusScheduleService scheduleService;

    @Autowired
    public AdminBusRestController(BusService busService, BusScheduleService scheduleService) {
        this.busService = busService;
        this.scheduleService = scheduleService;
    }

    @Operation(summary = "Get admin dashboard info")
    @GetMapping("/dashboard")
    public ResponseEntity<String> showAdminDashboard() {
        return ResponseEntity.ok("Admin dashboard API is running");
    }

    @Operation(summary = "Add a new Bus",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Bus created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid bus data", content = @Content)
            }
    )
    @PostMapping("/bus")
    public ResponseEntity<String> addBus(@RequestBody @Valid BusDTO busDTO) {
        busService.saveBus(busDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Bus added successfully");
    }

    @Operation(summary = "Get all Buses")
    @GetMapping("/bus")
    public ResponseEntity<List<BusDTO>> getAllBuses() {
        List<BusDTO> buses = busService.getAllBuses().stream()
                .map(bus -> {
                    BusDTO dto = new BusDTO();
                    dto.setId(bus.getId());
                    dto.setBusName(bus.getBusName());
                    return dto;
                })
                .toList();
        return ResponseEntity.ok(buses);
    }

    @Operation(summary = "Add a new Bus Schedule",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Schedule created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid schedule data", content = @Content)
            }
    )
    @PostMapping("/schedule")
    public ResponseEntity<?> addSchedule(@RequestBody @Valid BusScheduleDTO scheduleDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        Long busId = scheduleDTO.getBusId();
        if (busId == null) {
            return ResponseEntity.badRequest().body("Bus must be selected");
        }

        Bus bus = busService.getBusEntityById(busId);
        if (bus == null) {
            return ResponseEntity.badRequest().body("Selected bus not found");
        }

        scheduleService.addSchedule(scheduleDTO, bus);
        return ResponseEntity.status(HttpStatus.CREATED).body("Schedule added successfully");
    }

    @Operation(summary = "Get all Bus Schedules")
    @GetMapping("/schedule")
    public ResponseEntity<List<BusScheduleDTO>> getAllSchedules() {
        List<BusScheduleDTO> schedules = scheduleService.getAllSchedules();
        return ResponseEntity.ok(schedules);
    }
}
