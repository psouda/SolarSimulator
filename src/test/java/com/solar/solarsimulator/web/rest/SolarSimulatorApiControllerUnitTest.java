package com.solar.solarsimulator.web.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;

import com.solar.solarsimulator.model.SolarGrid;
import com.solar.solarsimulator.model.SolarNetworkState;
import com.solar.solarsimulator.model.response.TotalOutputResponse;
import com.solar.solarsimulator.config.AppProperties;
import com.solar.solarsimulator.service.SolarNetworkService;
import com.solar.solarsimulator.service.parser.CommandLineArgsParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
class SolarSimulatorApiControllerUnitTest {

    @MockBean
    private AppProperties appProperties;

    @MockBean
    private CommandLineArgsParser commandLineArgsParser;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SolarNetworkService solarNetworkService;

    private SolarGrid grid;

    @BeforeEach
    public void setup() {
        grid = new SolarGrid("Amsterdam", 100);
    }

    @Test
    void testLoadSolarNetwork() throws Exception {
        mockMvc.perform(post("/solar-simulator/load")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Collections.singletonList(grid))))
                .andExpect(status().isResetContent());
    }

    @Test
    void testGetTotalProducedPower() throws Exception {
        when(solarNetworkService.calculateProducedPower(13))
                .thenReturn(new TotalOutputResponse(9874.123));

        mockMvc.perform(get("/solar-simulator/output/10"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetNetworkState() throws Exception {
        when(solarNetworkService.getNetworkState(13))
                .thenReturn(new SolarNetworkState(List.of(grid), new TotalOutputResponse(4454.0)));

        mockMvc.perform(get("/solar-simulator/network/10"))
                .andExpect(status().isOk());
    }
}