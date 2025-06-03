package com.example.BusBookingApplication.Service.impl;

import com.example.BusBookingApplication.DTO.BusDTO;
import com.example.BusBookingApplication.DTO.BusScheduleDTO;
import com.example.BusBookingApplication.Entity.Bus;
import com.example.BusBookingApplication.Entity.BusSchedule;
import com.example.BusBookingApplication.Repository.BusRepository;
import com.example.BusBookingApplication.Repository.BusScheduleRepository;
import com.example.BusBookingApplication.Service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusServiceImpl implements BusService {

    private final BusRepository busRepository;
    private final BusScheduleRepository busScheduleRepository;

    @Autowired
    public BusServiceImpl(BusRepository busRepository, BusScheduleRepository busScheduleRepository) {
        this.busRepository = busRepository;
        this.busScheduleRepository = busScheduleRepository;
    }

    @Override
    public List<BusScheduleDTO> searchBuses(String source, String destination) {
        List<BusSchedule> schedules = busScheduleRepository.findBySourceAndDestination(source, destination);
        return schedules.stream().map(this::convertToScheduleDTO).collect(Collectors.toList());
    }

    @Override
    public BusDTO getBusById(Long id) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bus not found"));
        return convertToBusDTO(bus);
    }

    private BusDTO convertToBusDTO(Bus bus) {
        BusDTO dto = new BusDTO();
        dto.setId(bus.getId());
        dto.setBusName(bus.getBusName());
        dto.setOwner(bus.getOwner());
        dto.setType(bus.getType());
        dto.setTotalSeats(bus.getTotalSeats());

        List<BusScheduleDTO> scheduleDTOs = bus.getSchedules().stream()
                .map(this::convertToScheduleDTO)
                .collect(Collectors.toList());
        dto.setSchedules(scheduleDTOs);

        return dto;
    }

    private BusScheduleDTO convertToScheduleDTO(BusSchedule schedule) {
        BusScheduleDTO dto = new BusScheduleDTO();
        dto.setId(schedule.getId());
        dto.setDate(schedule.getDate());
        dto.setDepartureTime(schedule.getDepartureTime());
        dto.setArrivalTime(schedule.getArrivalTime());
        dto.setPrice(schedule.getPrice());
        dto.setSource(schedule.getSource());
        dto.setDestination(schedule.getDestination());
        dto.setBusId(schedule.getBus().getId());
        dto.setBusName(schedule.getBus().getBusName());
        return dto;
    }

    @Override
    public void saveBus(BusDTO busDTO) {
        Bus bus = new Bus();
        bus.setBusName(busDTO.getBusName());
        bus.setOwner(busDTO.getOwner());
        bus.setType(busDTO.getType());
        bus.setTotalSeats(busDTO.getTotalSeats());
        busRepository.save(bus);
    }

    @Override
    public Bus getBusEntityById(Long id) {
        return busRepository.findById(id).orElse(null);
    }

    @Override
    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }
}
