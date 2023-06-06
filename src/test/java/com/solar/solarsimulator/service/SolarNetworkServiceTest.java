package com.solar.solarsimulator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.solar.solarsimulator.exception.JsonFileIOException;
import com.solar.solarsimulator.mapper.SolarNetworkMapper;
import com.solar.solarsimulator.model.SolarGrid;
import com.solar.solarsimulator.model.response.TotalOutputResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class SolarNetworkServiceTest {

  private SolarNetworkService solarNetworkService;
  private SolarNetworkMapper solarNetworkMapper;

  @BeforeEach
  void setUp() {
    solarNetworkMapper = mock(SolarNetworkMapper.class);
    solarNetworkService = new SolarNetworkService(solarNetworkMapper);
  }

  @Test
  void testLoadNetworkByFilePath() {
    String path = "data/network.json";
    List<SolarGrid> mockNetwork = List.of(new SolarGrid("Amsterdam", 99));
    when(solarNetworkMapper.map(anyString())).thenReturn(mockNetwork);

    List<SolarGrid> result = solarNetworkService.loadNetworkByFilePath(path);

    assertEquals(mockNetwork, result);
    verify(solarNetworkMapper).map(anyString());
  }

  @Test
  void testCalculateProducedPower() {
    // 20*(1-(D/365*0,005))
    // 20*(1-(853/365*0.005)) + 20*(1-(852/365*0.005)) +
    // 20*(1-(472/365*0.005)) + 20*(1-(471/365*0.005))+
    // 20*(1-(252/365*0.005)) + 20*(1-(251/365*0.005))
    // 19766.3013699 + 19766.5753425 +
    // 19870.6849315 + 19870.9589041 +
    // 19930.9589041 + 19931.2328767
    // = 119136.7123288
    double actual = 119136.7123288;
    SolarGrid gridAmsterdam = new SolarGrid("Amsterdam", 854);
    SolarGrid gridGroningen = new SolarGrid("Groningen", 473);
    SolarGrid gridMaastricht = new SolarGrid("Maastricht", 253);
    solarNetworkService.setSolarNetwork(List.of(gridAmsterdam, gridGroningen, gridMaastricht));

    TotalOutputResponse response = solarNetworkService.calculateProducedPower(2);

    assertNotNull(response);
    assertThat(response.getTotalOutputInKwh()).isEqualTo(actual, offset(0.0001d));
  }

  @Test
  void testLoadSolarNetworkFromJsonFile() {
    MultipartFile file = new MockMultipartFile("file", "Hello, World!".getBytes());
    List<SolarGrid> mockNetwork = List.of(new SolarGrid("Amsterdam", 99));
    when(solarNetworkMapper.map(anyString())).thenReturn(mockNetwork);

    assertDoesNotThrow(() -> solarNetworkService.loadSolarNetworkFromJsonFile(file));

    verify(solarNetworkMapper).map(anyString());
  }

  @Test
  void testLoadSolarNetworkFromJsonFileThrowsException() {
    MultipartFile file = new MockMultipartFile("file", new byte[0]);

    doAnswer(
            invocation -> {
              throw new IOException("Test exception");
            })
        .when(solarNetworkMapper)
        .map(anyString());

    assertThrows(
        JsonFileIOException.class, () -> solarNetworkService.loadSolarNetworkFromJsonFile(file));
  }

  @Test
  void testAddGrid() {

    SolarGrid solarGrid = new SolarGrid("Amsterdam", 99);
    List<SolarGrid> network = new ArrayList<>();
    network.add(solarGrid);
    solarNetworkService.setSolarNetwork(network);
    solarNetworkService.addGrid("newGrid", 0);

    Optional<SolarGrid> result =
        solarNetworkService.getSolarNetwork().stream()
            .filter(grid -> grid.getName().equals("newGrid"))
            .findFirst();

    assertTrue(result.isPresent());
  }

  @Test
  void testRemoveGrid() {
    SolarGrid grid = new SolarGrid("Amsterdam", 99);
    List<SolarGrid> network = new ArrayList<>();
    network.add(grid);
    solarNetworkService.setSolarNetwork(network);

    solarNetworkService.removeGrid("Amsterdam");

    Optional<SolarGrid> result =
        solarNetworkService.getSolarNetwork().stream()
            .filter(g -> g.getName().equals("Amsterdam"))
            .findFirst();

    assertFalse(result.isPresent());
  }
}
