package com.example.BusBookingApplication.Service;

import com.example.BusBookingApplication.DTO.UserDTO;
import com.example.BusBookingApplication.Entity.User;
import com.example.BusBookingApplication.Repository.UserRepository;
import com.example.BusBookingApplication.Service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_ShouldSaveUser_WhenEmailIsNew() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("user@example.com");
        userDTO.setName("John");
        userDTO.setPassword("password");

        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User savedUser = userService.registerUser(userDTO);

        // Assert
        verify(userRepository).save(userCaptor.capture());
        assertEquals("John", savedUser.getName());
        assertEquals("user@example.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals("USER", savedUser.getRole());
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailExists() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("user@example.com");
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.registerUser(userDTO));
        assertEquals("Email is already registered. Please login.", exception.getMessage());
    }

    @Test
    void getUserByEmail_ShouldReturnUser_WhenFound() {
        // Arrange
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserByEmail(email);

        // Assert
        assertEquals(email, result.getEmail());
    }

    @Test
    void getUserByEmail_ShouldThrowException_WhenNotFound() {
        // Arrange
        when(userRepository.findByEmail("no@user.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.getUserByEmail("no@user.com"));
    }

    @Test
    void getUserDTOByEmail_ShouldReturnDTO_WhenUserExists() {
        // Arrange
        User user = new User();
        user.setEmail("user@example.com");
        user.setName("Jane");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        // Act
        UserDTO dto = userService.getUserDTOByEmail("user@example.com");

        // Assert
        assertEquals("user@example.com", dto.getEmail());
        assertEquals("Jane", dto.getName());
    }

    @Test
    void getUserDTOByEmail_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userService.getUserDTOByEmail("unknown@example.com"));
    }

    @Test
    void updateProfile_ShouldUpdateNameAndPassword_WhenPasswordGiven() {
        // Arrange
        String email = "user@example.com";
        User existingUser = new User();
        existingUser.setEmail(email);
        existingUser.setName("Old Name");

        UserDTO dto = new UserDTO();
        dto.setEmail(email);
        dto.setName("New Name");

        String newPassword = "newPass";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPass");

        // Act
        userService.updateProfile(dto, newPassword);

        // Assert
        verify(userRepository).save(existingUser);
        assertEquals("New Name", existingUser.getName());
        assertEquals("encodedNewPass", existingUser.getPassword());
    }

    @Test
    void updateProfile_ShouldOnlyUpdateName_WhenPasswordIsNullOrEmpty() {
        // Arrange
        User user = new User();
        user.setEmail("user@example.com");
        user.setName("Old");

        UserDTO dto = new UserDTO();
        dto.setEmail("user@example.com");
        dto.setName("Updated");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        // Act
        userService.updateProfile(dto, "");

        // Assert
        verify(userRepository).save(user);
        assertEquals("Updated", user.getName());
        assertNull(user.getPassword());  // if password was never set
    }

    @Test
    void updateProfile_ShouldThrowException_WhenUserNotFound() {
        UserDTO dto = new UserDTO();
        dto.setEmail("notfound@example.com");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.updateProfile(dto, "anyPassword"));
    }
}
