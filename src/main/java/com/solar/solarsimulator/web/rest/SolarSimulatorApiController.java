package com.solar.solarsimulator.web.rest;

import java.util.List;

import com.solar.solarsimulator.model.SolarGrid;
import com.solar.solarsimulator.model.SolarNetworkState;
import com.solar.solarsimulator.model.response.TotalOutputResponse;
import lombok.RequiredArgsConstructor;
import com.solar.solarsimulator.model.response.Response;
import com.solar.solarsimulator.service.SolarNetworkService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/solar-simulator")
@RequiredArgsConstructor
public class SolarSimulatorApiController {

  private final SolarNetworkService solarNetworkService;

  @GetMapping("/get-network")
  public Response<List<SolarGrid>> getSolarNetwork() {
    return Response.<List<SolarGrid>>builder()
        .data(solarNetworkService.getSolarNetwork())
        .status(HttpStatus.OK)
        .build();
  }

  @PostMapping("/load")
  @ResponseStatus(HttpStatus.RESET_CONTENT)
  public Response<Void> loadSolarNetwork(@RequestBody List<SolarGrid> solarGrids) {
    solarNetworkService.setSolarNetwork(solarGrids);
    return Response.<Void>builder().status(HttpStatus.RESET_CONTENT).build();
  }

  @GetMapping("/output/{T}")
  public Response<TotalOutputResponse> getTotalProducedPower(@PathVariable("T") int elapsedDays) {
    return Response.<TotalOutputResponse>builder()
        .data(solarNetworkService.calculateProducedPower(elapsedDays))
        .status(HttpStatus.OK)
        .build();
  }

  @GetMapping("/network/{T}")
  public Response<SolarNetworkState> getNetworkState(@PathVariable("T") int elapsedDays) {
    return Response.<SolarNetworkState>builder()
        .data(solarNetworkService.getNetworkState(elapsedDays))
        .status(HttpStatus.OK)
        .build();
  }
}
