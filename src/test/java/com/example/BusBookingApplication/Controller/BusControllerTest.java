package com.example.BusBookingApplication.Controller;

import com.example.BusBookingApplication.DTO.BusScheduleDTO;
import com.example.BusBookingApplication.Service.BusScheduleService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BusController.class)
public class BusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BusScheduleService busScheduleService;

    @Test
    void searchSchedules_WithValidParams_ShouldReturnSchedulesInModel() throws Exception {
        BusScheduleDTO dto = new BusScheduleDTO();
        dto.setId(1L);
        dto.setSource("CityA");
        dto.setDestination("CityB");
        dto.setDate(LocalDate.of(2025, 6, 5));

        when(busScheduleService.searchSchedulesDate(eq("CityA"), eq("CityB"), any(LocalDate.class)))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/buses/search")
                        .param("source", "CityA")
                        .param("destination", "CityB")
                        .param("date", "2025-06-05"))
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(model().attributeExists("schedules"))
                .andExpect(model().attribute("schedules", List.of(dto)));
    }

    @Test
    void searchSchedules_WithInvalidDate_ShouldReturnDateError() throws Exception {
        mockMvc.perform(get("/buses/search")
                        .param("source", "CityA")
                        .param("destination", "CityB")
                        .param("date", "invalid-date"))
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(model().attributeExists("dateError"))
                .andExpect(model().attribute("dateError", "Invalid date format"));
    }

    @Test
    void searchSchedules_WithoutParams_ShouldReturnViewWithoutSchedules() throws Exception {
        mockMvc.perform(get("/buses/search"))
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(model().attributeDoesNotExist("schedules"))
                .andExpect(model().attributeDoesNotExist("dateError"));
    }

    @Test
    void viewSchedule_WithValidId_ShouldReturnScheduleInModel() throws Exception {
        BusScheduleDTO dto = new BusScheduleDTO();
        dto.setId(1L);
        dto.setSource("CityA");
        dto.setDestination("CityB");
        dto.setDate(LocalDate.of(2025, 6, 5));

        when(busScheduleService.getScheduleById(1L)).thenReturn(dto);

        mockMvc.perform(get("/buses/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("book-bus"))
                .andExpect(model().attributeExists("schedule"))
                .andExpect(model().attribute("schedule", dto));
    }
}
