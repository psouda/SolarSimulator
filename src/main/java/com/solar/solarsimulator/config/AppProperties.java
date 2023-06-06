package com.solar.solarsimulator.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private boolean runWithArgs;

    public boolean isRunWithArgs() {
        return runWithArgs;
    }

    public void setRunWithArgs(boolean runWithArgs) {
        this.runWithArgs = runWithArgs;
    }
}