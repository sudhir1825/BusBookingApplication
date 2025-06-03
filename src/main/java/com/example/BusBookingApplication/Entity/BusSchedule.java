package com.example.BusBookingApplication.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class BusSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus;

    private String source;
    private String destination;

    private LocalDate date;
    private LocalTime departureTime;
    private LocalTime arrivalTime;

    private double price;
    private int totalSeats;

    @ElementCollection
    private Set<String> bookedSeats = new HashSet<>();


    public BusSchedule() {
    }

    public BusSchedule(Long id, Bus bus, String source, String destination, LocalDate date, LocalTime departureTime, LocalTime arrivalTime, double price, int totalSeats, Set<String> bookedSeats) {
        this.id = id;
        this.bus = bus;
        this.source = source;
        this.destination = destination;
        this.date = date;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.totalSeats = totalSeats;
        this.bookedSeats = bookedSeats;
    }
    // Getters and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Set<String> getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(Set<String> bookedSeats) {
        this.bookedSeats = bookedSeats;
    }
}

