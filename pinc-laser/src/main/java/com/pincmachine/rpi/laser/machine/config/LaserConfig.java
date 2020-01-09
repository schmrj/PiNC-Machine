package com.pincmachine.rpi.laser.machine.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "laser")
public class LaserConfig {

    @NestedConfigurationProperty
    private MotorConfig motorConfig;

    @NestedConfigurationProperty
    private Workspace workspace;

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public MotorConfig getMotorConfig() {
        return motorConfig;
    }

    public void setMotorConfig(MotorConfig motorConfig) {
        this.motorConfig = motorConfig;
    }
}
