package com.example.BusBookingApplication.Repository;

import com.example.BusBookingApplication.Entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusRepository extends JpaRepository<Bus, Long> {

}
