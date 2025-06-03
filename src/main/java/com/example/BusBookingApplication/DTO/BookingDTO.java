package com.example.BusBookingApplication.DTO;

import jakarta.validation.constraints.*;
import java.util.List;

public class BookingDTO {

    @NotNull(message = "Bus ID is required")
    private Long busScheduleId;

    @Email(message = "Enter a valid user email")
    private String userEmail;

    @Size(min = 1, message = "At least one passenger is required")
    private List<PassengerDTO> passengers;

    private List<String> selectedSeats;
    public BookingDTO() {
    }

    public BookingDTO(Long busScheduleId, String userEmail, List<PassengerDTO> passengers, List<String> selectedSeats) {
        this.busScheduleId = busScheduleId;
        this.userEmail = userEmail;
        this.passengers = passengers;
        this.selectedSeats = selectedSeats;
    }
    // Getters and Setters
    public Long getBusScheduleId() {
        return busScheduleId;
    }

    public void setBusScheduleId(Long busScheduleId) {
        this.busScheduleId = busScheduleId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<PassengerDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerDTO> passengers) {
        this.passengers = passengers;
    }

    public List<String> getSelectedSeats() {
        return selectedSeats;
    }

    public void setSelectedSeats(List<String> selectedSeats) {
        this.selectedSeats = selectedSeats;
    }
}
