package com.solar.solarsimulator.service.parser;

import org.springframework.stereotype.Component;

@Component
public class CommandLineArgsParser {

  public String parsePath(String[] args) {
    if (args.length < 1) {
      throw new IllegalArgumentException("Usage: java SolarSimulatorApplication <path_to_json_file> <elapsed_days>");
    }
    return args[0];
  }

  public int parseElapsedDays(String[] args) {
    if (args.length < 2) {
      throw new IllegalArgumentException("Usage: java SolarSimulatorApplication <path_to_json_file> <elapsed_days>");
    }
    try {
      return Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Elapsed days must be a valid integer.", e);
    }
  }
}
