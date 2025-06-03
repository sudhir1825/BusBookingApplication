package com.example.BusBookingApplication.Service;


import com.example.BusBookingApplication.DTO.UserDTO;
import com.example.BusBookingApplication.Entity.User;

public interface UserService {
    User registerUser(UserDTO userDTO);
    User getUserByEmail(String email); // Internal use
    UserDTO getUserDTOByEmail(String email); // For profile view
    void updateProfile(UserDTO dto, String newPassword);
}

