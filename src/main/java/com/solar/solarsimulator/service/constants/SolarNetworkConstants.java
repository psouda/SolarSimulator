package com.solar.solarsimulator.service.constants;

public class SolarNetworkConstants {
    private SolarNetworkConstants() {
        throw new IllegalStateException("Constant class");
    }
    public static final double FULL_SUN_HOURS = 1000.0;
    public static final double OPTIMAL_POWER_OUTPUT = 20.0;
    public static final double POWER_DECAY_RATE = 0.005;
    public static final int DAYS_IN_YEAR = 365;
    public static final int MINIMUM_OPERATIONAL_AGE = 60; // days
    public static final int MAXIMUM_OPERATIONAL_AGE = 25 * 365; // days
}
