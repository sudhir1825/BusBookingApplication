package com.example.BusBookingApplication.Service;

import com.example.BusBookingApplication.DTO.BusScheduleDTO;
import com.example.BusBookingApplication.Entity.Bus;

import java.time.LocalDate;
import java.util.List;

public interface BusScheduleService {

    void addSchedule(BusScheduleDTO dto, Bus bus);

    List<BusScheduleDTO> getAllSchedules();

    List<BusScheduleDTO> searchSchedulesDate(String source, String destination,LocalDate date);
    BusScheduleDTO getScheduleById(Long id);
}
