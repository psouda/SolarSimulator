package com.solar.solarsimulator.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.solar.solarsimulator.exception.JsonFileIOException;
import com.solar.solarsimulator.mapper.SolarNetworkMapper;
import com.solar.solarsimulator.model.SolarGrid;
import com.solar.solarsimulator.model.SolarNetworkState;
import com.solar.solarsimulator.model.response.TotalOutputResponse;
import com.solar.solarsimulator.service.constants.SolarNetworkConstants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import com.solar.solarsimulator.utils.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class SolarNetworkService {

  private final SolarNetworkMapper solarNetworkMapper;

  @Getter @Setter private List<SolarGrid> solarNetwork;

  public List<SolarGrid> loadNetworkByFilePath(String path) {
    solarNetwork = solarNetworkMapper.map(FileUtils.getStringFromFile(path));
    return solarNetwork;
  }

  public TotalOutputResponse calculateProducedPower(int elapsedDays) {
    double totalPower =
        solarNetwork.stream()
            .mapToDouble(grid -> calculateGridProducedPower(grid, elapsedDays))
            .sum();

    return new TotalOutputResponse(totalPower);
  }

  public SolarNetworkState getNetworkState(int elapsedDays) {
    return new SolarNetworkState(solarNetwork, calculateProducedPower(elapsedDays));
  }

  public void loadSolarNetworkFromJsonFile(MultipartFile jsonFile) {
    try {
      String content = new String(jsonFile.getBytes());
      solarNetwork = solarNetworkMapper.map(content);
    } catch (IOException exception) {
      throw new JsonFileIOException(
          "Error reading JSON file: " + exception.getMessage(), exception);
    }
  }

  public void addGrid(String gridName, int gridAge) {
    Optional<SolarGrid> optionalGrid =
        solarNetwork.stream().filter(grid -> grid.getName().equals(gridName)).findFirst();
    if (optionalGrid.isPresent()) {
      SolarGrid existingGrid = optionalGrid.get();
      existingGrid.setAge(gridAge);
    } else {
      SolarGrid solarGrid = new SolarGrid(gridName, gridAge);
      solarNetwork.add(solarGrid);
    }
  }

  public void removeGrid(String gridName) {
    solarNetwork.removeIf(solarGrid -> solarGrid.getName().equals(gridName));
  }

  public Map<String, Double> calculateProducedPowerForEachGrid(int elapsedDays) {
    Map<String, Double> outputMap = new HashMap<>();
    for (SolarGrid grid : solarNetwork) {
      double producedPower = calculateGridProducedPower(grid, elapsedDays);
      outputMap.put(grid.getName(), producedPower);
    }
    return outputMap;
  }

  private double calculateGridProducedPower(SolarGrid grid, int elapsedDays) {
    double gridProducedPower = 0.0;

    // Start from the age of the grid as of 'elapsedDays' ago
    int startAge = grid.getAge() - elapsedDays;
    for (int i = 0; i < elapsedDays; i++) {
      // Calculate the age of the grid for each day in the range
      int gridAge = startAge + i;

      if (gridAge > SolarNetworkConstants.MAXIMUM_OPERATIONAL_AGE) {
        break;
      }
      if (gridAge < SolarNetworkConstants.MINIMUM_OPERATIONAL_AGE) {
        continue;
      }

      // Calculate the power produced on this day and add it to the total
      double powerOutput = SolarNetworkConstants.OPTIMAL_POWER_OUTPUT * (1 - ((double) gridAge / SolarNetworkConstants.DAYS_IN_YEAR) * SolarNetworkConstants.POWER_DECAY_RATE);
      double producedPower = powerOutput * SolarNetworkConstants.FULL_SUN_HOURS;
      gridProducedPower += producedPower;
    }
    return gridProducedPower;
  }
}
