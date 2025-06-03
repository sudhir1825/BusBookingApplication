package com.example.BusBookingApplication.Entity;

import jakarta.persistence.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "buses")
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String busName;

    private String owner;
    private String type;
    private int totalSeats;
    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL)
    private List<BusSchedule> schedules;

    public Bus() {
    }

    public Bus(Long id, String busName, String owner, String type, int totalSeats, List<BusSchedule> schedules) {
        this.id = id;
        this.busName = busName;
        this.owner = owner;
        this.type = type;
        this.totalSeats = totalSeats;
        this.schedules = schedules;
    }

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

    public List<BusSchedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<BusSchedule> schedules) {
        this.schedules = schedules;
    }
}

