package com.example.BusBookingApplication.Controller;

import com.example.BusBookingApplication.DTO.BusDTO;
import com.example.BusBookingApplication.DTO.BusScheduleDTO;
import com.example.BusBookingApplication.Entity.Bus;
import com.example.BusBookingApplication.Service.BusScheduleService;
import com.example.BusBookingApplication.Service.BusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AdminBusController.class)
public class AdminBusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BusService busService;

    @MockBean
    private BusScheduleService scheduleService;

    private Bus bus;

    @BeforeEach
    public void setup() {
        bus = new Bus();
        bus.setId(1L);
        bus.setBusName("Test Bus");
        bus.setOwner("Owner");
        bus.setType("AC");
        bus.setTotalSeats(40);
    }

    @Test
    public void testShowAdminDashboard() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/dashboard"));
    }

    @Test
    public void testShowAddBusForm() throws Exception {
        mockMvc.perform(get("/admin/bus/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/add-bus"))
                .andExpect(model().attributeExists("bus"));
    }

    @Test
    public void testProcessAddBus() throws Exception {
        mockMvc.perform(post("/admin/bus/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("busName", "New Bus")
                        .param("owner", "Owner Name")
                        .param("type", "Non-AC")
                        .param("totalSeats", "30")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/bus/add?success"));

        // Verify service method called with any BusDTO
        verify(busService).saveBus(any(BusDTO.class));
    }

    @Test
    public void testShowAddScheduleForm() throws Exception {
        when(busService.getAllBuses()).thenReturn(Collections.singletonList(bus));

        mockMvc.perform(get("/admin/schedule/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/add-schedule"))
                .andExpect(model().attributeExists("schedule"))
                .andExpect(model().attribute("buses", hasSize(1)))
                .andExpect(model().attribute("buses", hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("busName", is("Test Bus"))
                        )
                )));
    }

    @Test
    public void testProcessAddSchedule_Success() throws Exception {
        when(busService.getAllBuses()).thenReturn(Collections.singletonList(bus));
        when(busService.getBusEntityById(1L)).thenReturn(bus);

        mockMvc.perform(post("/admin/schedule/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("busId", "1")
                        .param("source", "City A")
                        .param("destination", "City B")
                        .param("date", "2025-06-05")
                        .param("departureTime", "09:00")
                        .param("arrivalTime", "12:00")
                        .param("price", "500")
                        .param("availableSeats", "40")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/schedule/add?success"));

        // Capture the DTO argument passed to addSchedule()
        ArgumentCaptor<BusScheduleDTO> captor = ArgumentCaptor.forClass(BusScheduleDTO.class);
        verify(scheduleService).addSchedule(captor.capture(), eq(bus));

        BusScheduleDTO dto = captor.getValue();
        // Basic assertions on DTO
        assert(dto.getBusId().equals(1L));
        assert(dto.getSource().equals("City A"));
        assert(dto.getDestination().equals("City B"));
        assert(dto.getPrice() == 500);
        assert(dto.getAvailableSeats() == 40);
    }

    @Test
    public void testProcessAddSchedule_MissingBusId() throws Exception {
        when(busService.getAllBuses()).thenReturn(Collections.singletonList(bus));

        mockMvc.perform(post("/admin/schedule/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("source", "City A")
                        .param("destination", "City B")
                        .param("date", "2025-06-05")
                        .param("departureTime", "09:00")
                        .param("arrivalTime", "12:00")
                        .param("price", "500")
                        .param("availableSeats", "40")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("admin/add-schedule"))
                .andExpect(model().attributeHasFieldErrors("schedule", "busId"))
                .andExpect(model().attributeExists("buses"));
    }

    @Test
    public void testProcessAddSchedule_InvalidBusId() throws Exception {
        when(busService.getAllBuses()).thenReturn(Collections.singletonList(bus));
        when(busService.getBusEntityById(99L)).thenReturn(null);

        mockMvc.perform(post("/admin/schedule/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("busId", "99")
                        .param("source", "City A")
                        .param("destination", "City B")
                        .param("date", "2025-06-05")
                        .param("departureTime", "09:00")
                        .param("arrivalTime", "12:00")
                        .param("price", "500")
                        .param("availableSeats", "40")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("admin/add-schedule"))
                .andExpect(model().attributeHasFieldErrors("schedule", "busId"))
                .andExpect(model().attributeExists("buses"));
    }
}
