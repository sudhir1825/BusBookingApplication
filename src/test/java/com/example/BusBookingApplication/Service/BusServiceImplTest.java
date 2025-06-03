package com.example.BusBookingApplication.Service;

import com.example.BusBookingApplication.DTO.BusDTO;
import com.example.BusBookingApplication.DTO.BusScheduleDTO;
import com.example.BusBookingApplication.Entity.Bus;
import com.example.BusBookingApplication.Entity.BusSchedule;
import com.example.BusBookingApplication.Repository.BusRepository;
import com.example.BusBookingApplication.Repository.BusScheduleRepository;
import com.example.BusBookingApplication.Service.impl.BusServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BusServiceImplTest {

    @Mock
    private BusRepository busRepository;

    @Mock
    private BusScheduleRepository busScheduleRepository;

    @InjectMocks
    private BusServiceImpl busService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void searchBuses_ShouldReturnMatchingSchedules() {
        // Arrange
        String source = "CityA";
        String destination = "CityB";

        Bus bus = new Bus();
        bus.setId(1L);
        bus.setBusName("Super Travels");

        BusSchedule schedule = new BusSchedule();
        schedule.setId(100L);
        schedule.setBus(bus);
        schedule.setSource(source);
        schedule.setDestination(destination);
        schedule.setDate(LocalDate.now());
        schedule.setDepartureTime(LocalTime.of(10, 0));
        schedule.setArrivalTime(LocalTime.of(14, 0));
        schedule.setPrice(200.0);

        when(busScheduleRepository.findBySourceAndDestination(source, destination))
                .thenReturn(List.of(schedule));

        // Act
        List<BusScheduleDTO> results = busService.searchBuses(source, destination);

        // Assert
        assertEquals(1, results.size());
        assertEquals("CityA", results.get(0).getSource());
        assertEquals("CityB", results.get(0).getDestination());
        assertEquals("Super Travels", results.get(0).getBusName());
    }

    @Test
    void getBusById_ShouldReturnBusDTO_WhenFound() {
        // Arrange
        Long busId = 1L;

        Bus bus = new Bus();
        bus.setId(busId);
        bus.setBusName("Blue Line");
        bus.setOwner("Mr. Kumar");
        bus.setType("AC");
        bus.setTotalSeats(50);

        BusSchedule schedule = new BusSchedule();
        schedule.setId(1L);
        schedule.setBus(bus);
        schedule.setSource("CityA");
        schedule.setDestination("CityB");
        schedule.setDate(LocalDate.now());
        schedule.setDepartureTime(LocalTime.of(8, 0));
        schedule.setArrivalTime(LocalTime.of(12, 0));
        schedule.setPrice(300.0);

        bus.setSchedules(List.of(schedule));

        when(busRepository.findById(busId)).thenReturn(Optional.of(bus));

        // Act
        BusDTO result = busService.getBusById(busId);

        // Assert
        assertNotNull(result);
        assertEquals("Blue Line", result.getBusName());
        assertEquals(1, result.getSchedules().size());
        assertEquals("CityA", result.getSchedules().get(0).getSource());
    }

    @Test
    void getBusById_ShouldThrowException_WhenNotFound() {
        // Arrange
        Long busId = 999L;
        when(busRepository.findById(busId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> busService.getBusById(busId));
    }

    @Test
    void saveBus_ShouldPersistBus() {
        // Arrange
        BusDTO busDTO = new BusDTO();
        busDTO.setBusName("Red Express");
        busDTO.setOwner("Ms. Asha");
        busDTO.setType("Non-AC");
        busDTO.setTotalSeats(40);

        // Act
        busService.saveBus(busDTO);

        // Assert
        verify(busRepository, times(1)).save(any(Bus.class));
    }

    @Test
    void getBusEntityById_ShouldReturnBus_WhenFound() {
        // Arrange
        Long busId = 1L;
        Bus bus = new Bus();
        bus.setId(busId);

        when(busRepository.findById(busId)).thenReturn(Optional.of(bus));

        // Act
        Bus result = busService.getBusEntityById(busId);

        // Assert
        assertNotNull(result);
        assertEquals(busId, result.getId());
    }

    @Test
    void getBusEntityById_ShouldReturnNull_WhenNotFound() {
        // Arrange
        Long busId = 2L;
        when(busRepository.findById(busId)).thenReturn(Optional.empty());

        // Act
        Bus result = busService.getBusEntityById(busId);

        // Assert
        assertNull(result);
    }

    @Test
    void getAllBuses_ShouldReturnListOfBuses() {
        // Arrange
        Bus bus1 = new Bus();
        Bus bus2 = new Bus();
        when(busRepository.findAll()).thenReturn(List.of(bus1, bus2));

        // Act
        List<Bus> result = busService.getAllBuses();

        // Assert
        assertEquals(2, result.size());
    }
}
