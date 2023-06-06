package com.solar.solarsimulator.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solar.solarsimulator.exception.JsonParsingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SolarNetworkMapperTest {

  private final SolarNetworkMapper solarNetworkMapper = new SolarNetworkMapper(new ObjectMapper());

  @Test
  void shouldThrowJsonParsingExceptionWhenJsonStringIsNull() {
    assertThrows(JsonParsingException.class, () -> solarNetworkMapper.map(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {"[{\"name\": \"Amsterdam\", \"age\": \"invalid\"}]", "[", ""})
  void shouldThrowJsonParsingExceptionWhenJsonStringIsInvalid(String jsonString) {
    assertThrows(JsonParsingException.class, () -> solarNetworkMapper.map(jsonString));
  }
}
