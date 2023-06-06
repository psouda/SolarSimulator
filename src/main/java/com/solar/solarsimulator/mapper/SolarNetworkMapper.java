package com.solar.solarsimulator.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

import com.solar.solarsimulator.exception.JsonParsingException;
import com.solar.solarsimulator.model.SolarGrid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SolarNetworkMapper {

  private final ObjectMapper mapper;

  public List<SolarGrid> map(String json) {
    try {
      return mapper.readValue(json, new TypeReference<>() {});
    } catch (JsonProcessingException | IllegalArgumentException exception) {
      throw new JsonParsingException("Error parsing JSON string: " + exception.getMessage(), exception);
    }
  }
}
