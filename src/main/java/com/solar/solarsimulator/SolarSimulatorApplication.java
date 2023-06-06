package com.solar.solarsimulator;

import com.solar.solarsimulator.model.SolarGrid;
import com.solar.solarsimulator.model.response.TotalOutputResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.solar.solarsimulator.config.AppProperties;
import com.solar.solarsimulator.service.SolarNetworkService;
import com.solar.solarsimulator.service.parser.CommandLineArgsParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class SolarSimulatorApplication implements CommandLineRunner {

    private final SolarNetworkService solarNetworkService;
    private final AppProperties appProperties;
    private final CommandLineArgsParser commandLineArgsParser;

    public static void main(String[] args) {
        SpringApplication.run(SolarSimulatorApplication.class, args);
    }

    @Override
    public void run(String... args) {
        if (appProperties.isRunWithArgs()) {
            printTheNetwork(solarNetworkService.loadNetworkByFilePath(commandLineArgsParser.parsePath(args)));
            printProducedPower(solarNetworkService.calculateProducedPower(commandLineArgsParser.parseElapsedDays(args)));
        }
    }

    private void printTheNetwork(List<SolarGrid> solarNetwork) {
        solarNetwork.forEach(grid -> log.info("  " + grid.getName() + " in use for " + grid.getAge() + " days"));
    }

    private void printProducedPower(TotalOutputResponse totalOutputResponse) {
        log.info("Produced: " + totalOutputResponse.getTotalOutputInKwh() + " kWh");
    }
}
