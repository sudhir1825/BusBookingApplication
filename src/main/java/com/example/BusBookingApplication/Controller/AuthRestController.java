package com.example.BusBookingApplication.Controller;

import com.example.BusBookingApplication.DTO.UserDTO;
import com.example.BusBookingApplication.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final UserService userService;

    @Autowired
    public AuthRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            userService.registerUser(userDTO);
            return ResponseEntity.ok("Registration successful. Please log in.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        UserDTO user = userService.getUserDTOByEmail(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody UserDTO userDTO,
                                               @RequestParam(required = false) String newPassword) {
        userService.updateProfile(userDTO, newPassword);
        return ResponseEntity.ok("Profile updated successfully.");
    }

    @GetMapping("/status")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Auth API is up and running");
    }
}
