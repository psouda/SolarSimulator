package com.solar.solarsimulator.web;

import java.util.List;

import com.solar.solarsimulator.model.SolarGrid;
import lombok.RequiredArgsConstructor;
import com.solar.solarsimulator.service.SolarNetworkService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class SolarSimulatorThymeleafController {

    public static final String SOLAR_SIMULATOR = "solar-simulator";
    public static final String REDIRECT_SOLAR_SIMULATOR = "redirect:/solar-simulator";
    private final SolarNetworkService solarNetworkService;

    @GetMapping({"/", "/solar-simulator"})
    public String solarSimulator(Model model) {
        List<SolarGrid> solarGrids = solarNetworkService.getSolarNetwork();
        List<String> gridNames = solarGrids.stream().map(SolarGrid::getName).toList();
        model.addAttribute("solarGrids", solarGrids);
        model.addAttribute("gridNames", gridNames);
        model.addAttribute("gridsOutput", solarNetworkService.calculateProducedPowerForEachGrid(1));
        return SOLAR_SIMULATOR;
    }

    @PostMapping("/add-grid-json")
    public String addGridsFromJson(MultipartFile jsonFile) {
        solarNetworkService.loadSolarNetworkFromJsonFile(jsonFile);
        return REDIRECT_SOLAR_SIMULATOR;
    }

    @PostMapping("/add-grid")
    public String addGrid(@RequestParam("gridName") String gridName, @RequestParam("gridAge") int gridAge) {
        solarNetworkService.addGrid(gridName, gridAge);
        return REDIRECT_SOLAR_SIMULATOR;
    }

    @PostMapping("/remove-grid")
    public String removeGrid(@RequestParam("gridName") String gridName) {
        solarNetworkService.removeGrid(gridName);
        return REDIRECT_SOLAR_SIMULATOR;
    }

}
