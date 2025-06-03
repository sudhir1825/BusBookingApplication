package com.example.BusBookingApplication.DTO;

import jakarta.validation.constraints.*;

public class PassengerDTO {

    @NotBlank(message = "Passenger name is required")
    private String name;

    @Min(value = 1, message = "Age must be greater than 0")
    private int age;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    public PassengerDTO() {
    }

    public PassengerDTO(String name, int age, String gender, String seatNumber) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.seatNumber = seatNumber;
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
}
