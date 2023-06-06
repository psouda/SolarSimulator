package com.solar.solarsimulator.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.solar.solarsimulator.model.SolarGrid;
import com.solar.solarsimulator.model.response.Response;
import com.solar.solarsimulator.model.response.TotalOutputResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SolarNetworkServiceIntegrationTest {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  private String baseUrl;

  @BeforeEach
  void setBaseUrl() {
    baseUrl = "http://localhost:" + port + "/solar-simulator";
  }

  @Test
  void testLoadSolarNetworkFromJsonFile() throws IOException {

    Path jsonPath = Paths.get("data/network.json");
    String content = Files.readString(jsonPath);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> request = new HttpEntity<>(content, headers);

    baseUrl = "http://localhost:" + port + "/solar-simulator";
    ResponseEntity<String> response =
        restTemplate.postForEntity(baseUrl + "/load", request, String.class);

    assertEquals(HttpStatus.RESET_CONTENT, response.getStatusCode());
  }

  @Test
  void testGetNetwork() {
    ResponseEntity<Response<SolarGrid>> response =
        restTemplate.exchange(
            baseUrl + "/get-network", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void testCalculateTotalProducedPower() throws IOException {
    // 20*(1-(D/365*0,005))
    // 20*(1-(853/365*0.005)) + 20*(1-(472/365*0.005)) + 20*(1-(252/365*0.005))
    // 19766.3013699 + 19870.6849315 + 19930.9589041 = 59567.9452055
    double actual = 59567.9452055;
    Path jsonPath = Paths.get("data/network.json");
    String content = Files.readString(jsonPath);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> request = new HttpEntity<>(content, headers);

    baseUrl = "http://localhost:" + port + "/solar-simulator";
    ResponseEntity<String> response =
        restTemplate.postForEntity(baseUrl + "/load", request, String.class);

    assertEquals(HttpStatus.RESET_CONTENT, response.getStatusCode());

    ResponseEntity<Response<TotalOutputResponse>> responseOfProducedPower =
        restTemplate.exchange(
            baseUrl + "/output/1", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
    assertThat(responseOfProducedPower.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseOfProducedPower.getBody()).isNotNull();
    assertThat(responseOfProducedPower.getBody().getData()).isNotNull();
    assertThat(responseOfProducedPower.getBody().getData().getTotalOutputInKwh()).isEqualTo(actual, offset(0.0001d));
  }
}
