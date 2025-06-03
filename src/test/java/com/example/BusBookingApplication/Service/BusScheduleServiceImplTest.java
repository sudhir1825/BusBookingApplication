package com.example.BusBookingApplication.Service;

import com.example.BusBookingApplication.DTO.BusScheduleDTO;
import com.example.BusBookingApplication.Entity.Bus;
import com.example.BusBookingApplication.Entity.BusSchedule;
import com.example.BusBookingApplication.Repository.BusRepository;
import com.example.BusBookingApplication.Repository.BusScheduleRepository;
import com.example.BusBookingApplication.Service.impl.BusScheduleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BusScheduleServiceImplTest {

    @Mock
    private BusScheduleRepository scheduleRepository;

    @Mock
    private BusRepository busRepository;

    @InjectMocks
    private BusScheduleServiceImpl scheduleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addSchedule_ShouldSaveSchedule() {
        // Arrange
        Bus bus = new Bus();
        bus.setId(1L);

        BusScheduleDTO dto = new BusScheduleDTO();
        dto.setSource("CityA");
        dto.setDestination("CityB");
        dto.setDate(LocalDate.of(2025, 6, 15));
        dto.setDepartureTime(LocalTime.of(10, 0));
        dto.setArrivalTime(LocalTime.of(14, 0));
        dto.setPrice(150.0);
        dto.setAvailableSeats(40);

        // Act
        scheduleService.addSchedule(dto, bus);

        // Assert
        verify(scheduleRepository, times(1)).save(any(BusSchedule.class));
    }

    @Test
    void getAllSchedules_ShouldReturnDTOList() {
        // Arrange
        Bus bus = new Bus();
        bus.setId(1L);

        BusSchedule schedule = new BusSchedule();
        schedule.setId(100L);
        schedule.setBus(bus);
        schedule.setSource("CityA");
        schedule.setDestination("CityB");
        schedule.setDate(LocalDate.of(2025, 6, 15));
        schedule.setDepartureTime(LocalTime.of(10, 0));
        schedule.setArrivalTime(LocalTime.of(14, 0));
        schedule.setPrice(200.0);
        schedule.setTotalSeats(45);

        when(scheduleRepository.findAll()).thenReturn(List.of(schedule));

        // Act
        List<BusScheduleDTO> result = scheduleService.getAllSchedules();

        // Assert
        assertEquals(1, result.size());
        BusScheduleDTO dto = result.get(0);
        assertEquals("CityA", dto.getSource());
        assertEquals("CityB", dto.getDestination());
        assertEquals(200.0, dto.getPrice());
    }

    @Test
    void searchSchedulesDate_ShouldReturnMatchingDTOs() {
        // Arrange
        String source = "CityA";
        String destination = "CityB";
        LocalDate date = LocalDate.of(2025, 6, 15);

        Bus bus = new Bus();
        bus.setId(1L);

        BusSchedule schedule = new BusSchedule();
        schedule.setId(200L);
        schedule.setBus(bus);
        schedule.setSource(source);
        schedule.setDestination(destination);
        schedule.setDate(date);
        schedule.setDepartureTime(LocalTime.of(9, 0));
        schedule.setArrivalTime(LocalTime.of(12, 0));
        schedule.setPrice(180.0);
        schedule.setTotalSeats(50);

        when(scheduleRepository.findBySourceAndDestinationAndDate(source, destination, date))
                .thenReturn(List.of(schedule));

        // Act
        List<BusScheduleDTO> result = scheduleService.searchSchedulesDate(source, destination, date);

        // Assert
        assertEquals(1, result.size());
        assertEquals("CityA", result.get(0).getSource());
        assertEquals(180.0, result.get(0).getPrice());
    }

    @Test
    void getScheduleById_ShouldReturnDTO_WhenFound() {
        // Arrange
        Long scheduleId = 300L;

        Bus bus = new Bus();
        bus.setId(2L);

        BusSchedule schedule = new BusSchedule();
        schedule.setId(scheduleId);
        schedule.setBus(bus);
        schedule.setSource("CityX");
        schedule.setDestination("CityY");
        schedule.setDate(LocalDate.of(2025, 7, 1));
        schedule.setDepartureTime(LocalTime.of(8, 0));
        schedule.setArrivalTime(LocalTime.of(12, 0));
        schedule.setPrice(220.0);
        schedule.setTotalSeats(30);

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));

        // Act
        BusScheduleDTO result = scheduleService.getScheduleById(scheduleId);

        // Assert
        assertNotNull(result);
        assertEquals(scheduleId, result.getId());
        assertEquals("CityX", result.getSource());
    }

    @Test
    void getScheduleById_ShouldReturnNull_WhenNotFound() {
        // Arrange
        Long scheduleId = 404L;
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

        // Act
        BusScheduleDTO result = scheduleService.getScheduleById(scheduleId);

        // Assert
        assertNull(result);
    }
}
