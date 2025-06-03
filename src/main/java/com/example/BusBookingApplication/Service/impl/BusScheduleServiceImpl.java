package com.example.BusBookingApplication.Service.impl;

import com.example.BusBookingApplication.DTO.BusScheduleDTO;
import com.example.BusBookingApplication.Entity.Bus;
import com.example.BusBookingApplication.Entity.BusSchedule;
import com.example.BusBookingApplication.Repository.BusRepository;
import com.example.BusBookingApplication.Repository.BusScheduleRepository;
import com.example.BusBookingApplication.Service.BusScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BusScheduleServiceImpl implements BusScheduleService {

    private final BusScheduleRepository scheduleRepository;
    private final BusRepository busRepository;

    @Autowired
    public BusScheduleServiceImpl(BusScheduleRepository scheduleRepository, BusRepository busRepository) {
        this.scheduleRepository = scheduleRepository;
        this.busRepository = busRepository;
    }

    public void addSchedule(BusScheduleDTO scheduleDTO, Bus bus) {
        BusSchedule schedule = new BusSchedule();

        schedule.setBus(bus);
        schedule.setSource(scheduleDTO.getSource());
        schedule.setDestination(scheduleDTO.getDestination());
        schedule.setDate(scheduleDTO.getDate());
        schedule.setDepartureTime(scheduleDTO.getDepartureTime());
        schedule.setArrivalTime(scheduleDTO.getArrivalTime());
        schedule.setPrice(scheduleDTO.getPrice());
        schedule.setTotalSeats(scheduleDTO.getAvailableSeats());

        scheduleRepository.save(schedule);
    }
    @Override
    public List<BusScheduleDTO> getAllSchedules() {
        List<BusSchedule> schedules = scheduleRepository.findAll();
        List<BusScheduleDTO> dtoList = new ArrayList<>();

        for (BusSchedule s : schedules) {
            BusScheduleDTO dto = new BusScheduleDTO();
            dto.setId(s.getId());
            dto.setBusId(s.getBus().getId());
            dto.setSource(s.getSource());
            dto.setDestination(s.getDestination());
            dto.setDate(s.getDate());
            dto.setDepartureTime(s.getDepartureTime());
            dto.setArrivalTime(s.getArrivalTime());
            dto.setPrice(s.getPrice());
            dto.setAvailableSeats(s.getTotalSeats());
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public List<BusScheduleDTO> searchSchedulesDate(String source, String destination, LocalDate date) {
        List<BusSchedule> schedules = scheduleRepository.findBySourceAndDestinationAndDate(source, destination, date);
        List<BusScheduleDTO> dtoList = new ArrayList<>();

        for (BusSchedule s : schedules) {
            BusScheduleDTO dto = new BusScheduleDTO();
            dto.setId(s.getId());
            dto.setBusId(s.getBus().getId());
            dto.setSource(s.getSource());
            dto.setDestination(s.getDestination());
            dto.setDate(s.getDate());
            dto.setDepartureTime(s.getDepartureTime());
            dto.setArrivalTime(s.getArrivalTime());
            dto.setPrice(s.getPrice());
            dto.setAvailableSeats(s.getTotalSeats());
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public BusScheduleDTO getScheduleById(Long id) {
        BusSchedule s = scheduleRepository.findById(id).orElse(null);
        if (s == null) return null;

        BusScheduleDTO dto = new BusScheduleDTO();
        dto.setId(s.getId());
        dto.setBusId(s.getBus().getId());
        dto.setSource(s.getSource());
        dto.setDestination(s.getDestination());
        dto.setDate(s.getDate());
        dto.setDepartureTime(s.getDepartureTime());
        dto.setArrivalTime(s.getArrivalTime());
        dto.setPrice(s.getPrice());
        dto.setAvailableSeats(s.getTotalSeats());
        return dto;
    }
}
