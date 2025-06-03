package com.example.BusBookingApplication.Repository;


import com.example.BusBookingApplication.Entity.BusSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BusScheduleRepository extends JpaRepository<BusSchedule, Long> {
    List<BusSchedule> findBySourceAndDestinationAndDate(String source, String destination, LocalDate date);

    List<BusSchedule> findBySourceAndDestination(String source, String destination);
}
