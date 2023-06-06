package com.solar.solarsimulator.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.solar.solarsimulator.model.response.TotalOutputResponse;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolarNetworkState {

    private List<SolarGrid> solarNetwork;
    private TotalOutputResponse totalProducedPower;

}
