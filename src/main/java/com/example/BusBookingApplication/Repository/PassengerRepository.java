package com.example.BusBookingApplication.Repository;

import com.example.BusBookingApplication.Entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}

