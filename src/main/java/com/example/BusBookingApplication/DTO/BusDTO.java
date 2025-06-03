package com.example.BusBookingApplication.DTO;

import java.util.List;

public class BusDTO {

    private Long id;
    private String busName;
    private String owner;
    private String type;
    private int totalSeats;
    private List<BusScheduleDTO> schedules;

    public BusDTO() {}
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public List<BusScheduleDTO> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<BusScheduleDTO> schedules) {
        this.schedules = schedules;
    }
}
