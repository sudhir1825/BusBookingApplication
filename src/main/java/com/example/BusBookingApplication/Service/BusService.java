package com.example.BusBookingApplication.Service;



import com.example.BusBookingApplication.DTO.BusDTO;
import com.example.BusBookingApplication.DTO.BusScheduleDTO;
import com.example.BusBookingApplication.Entity.Bus;

import java.util.List;

public interface BusService {
    List<BusScheduleDTO> searchBuses(String source, String destination);
    BusDTO getBusById(Long id);

    // For Admin
    void saveBus(BusDTO busDTO);
    List<Bus> getAllBuses();

    public Bus getBusEntityById(Long id);
}
