package com.example.BusBookingApplication.Repository;

import com.example.BusBookingApplication.Entity.Booking;
import com.example.BusBookingApplication.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
    @Query("SELECT p.seatNumber FROM Passenger p WHERE p.booking.busSchedule.id = :scheduleId")
    List<String> findSeatsByScheduleId(@Param("scheduleId") Long scheduleId);


}
