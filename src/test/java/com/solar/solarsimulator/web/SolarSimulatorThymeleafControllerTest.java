package com.solar.solarsimulator.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import com.solar.solarsimulator.model.SolarGrid;
import com.solar.solarsimulator.service.SolarNetworkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

class SolarSimulatorThymeleafControllerTest {


    @Mock
    private SolarNetworkService solarNetworkService;

    @Mock
    private Model model;

    private SolarSimulatorThymeleafController controller;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        controller = new SolarSimulatorThymeleafController(solarNetworkService);
    }
    @Test
    void testSolarSimulator() {
        SolarGrid grid = new SolarGrid("Amsterdam", 100);
        when(solarNetworkService.getSolarNetwork()).thenReturn(List.of(grid));

        String result = controller.solarSimulator(model);

        verify(model, times(1)).addAttribute("solarGrids", List.of(grid));
        verify(model, times(1)).addAttribute("gridNames", List.of("Amsterdam"));
        verify(model, times(1)).addAttribute("gridsOutput", solarNetworkService.calculateProducedPowerForEachGrid(1));
        assertEquals(SolarSimulatorThymeleafController.SOLAR_SIMULATOR, result);
    }

    @Test
    void testAddGridsFromJson() {
        MultipartFile jsonFile = new MockMultipartFile("jsonFile", "", "application/json", "{\"name\":\"Test\"}".getBytes());

        String result = controller.addGridsFromJson(jsonFile);

        verify(solarNetworkService, times(1)).loadSolarNetworkFromJsonFile(jsonFile);
        assertEquals(SolarSimulatorThymeleafController.REDIRECT_SOLAR_SIMULATOR, result);
    }

    @Test
    void testAddGrid() {
        String gridName = "Amsterdam";
        int gridAge = 10;

        String result = controller.addGrid(gridName, gridAge);

        verify(solarNetworkService, times(1)).addGrid(gridName, gridAge);
        assertEquals(SolarSimulatorThymeleafController.REDIRECT_SOLAR_SIMULATOR, result);
    }

    @Test
    void testRemoveGrid() {
        String gridName = "Amsterdam";

        String result = controller.removeGrid(gridName);

        verify(solarNetworkService, times(1)).removeGrid(gridName);
        assertEquals(SolarSimulatorThymeleafController.REDIRECT_SOLAR_SIMULATOR, result);
    }
}
