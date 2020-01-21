package com.pincmachine.rpi.laser.machine.config;

import com.pi4j.io.gpio.PinState;
import org.springframework.stereotype.Component;

@Component
public class MotorConfig {

    private int xPin;
    private int xPinDir;
    private int xPinEndstop;

    private int yPin;
    private int yPinDir;
    private int yPinEndstop;

    private String stepMode;
    private int stepM0Pin;
    private int stepM1Pin;
    private int stepM2Pin;

    private String stepDistanceUnit;
    private double stepResolution;

    public static PinState CLOCKWISE = PinState.LOW;
    public static PinState COUNTER_CLOCKWISE = PinState.HIGH;

    public int getxPin() {
        return xPin;
    }

    public void setxPin(int xPin) {
        this.xPin = xPin;
    }

    public int getxPinDir() {
        return xPinDir;
    }

    public void setxPinDir(int xPinDir) {
        this.xPinDir = xPinDir;
    }

    public int getxPinEndstop() {
        return xPinEndstop;
    }

    public void setxPinEndstop(int xPinEndstop) {
        this.xPinEndstop = xPinEndstop;
    }

    public int getyPin() {
        return yPin;
    }

    public void setyPin(int yPin) {
        this.yPin = yPin;
    }

    public int getyPinDir() {
        return yPinDir;
    }

    public void setyPinDir(int yPinDir) {
        this.yPinDir = yPinDir;
    }

    public int getyPinEndstop() {
        return yPinEndstop;
    }

    public void setyPinEndstop(int yPinEndstop) {
        this.yPinEndstop = yPinEndstop;
    }

    public String getStepMode() {
        return stepMode;
    }

    public void setStepMode(String stepMode) {
        this.stepMode = stepMode;
    }

    public int getStepM0Pin() {
        return stepM0Pin;
    }

    public void setStepM0Pin(int stepM0Pin) {
        this.stepM0Pin = stepM0Pin;
    }

    public int getStepM1Pin() {
        return stepM1Pin;
    }

    public void setStepM1Pin(int stepM1Pin) {
        this.stepM1Pin = stepM1Pin;
    }

    public int getStepM2Pin() {
        return stepM2Pin;
    }

    public void setStepM2Pin(int stepM2Pin) {
        this.stepM2Pin = stepM2Pin;
    }

    public String getStepDistanceUnit() {
        return stepDistanceUnit;
    }

    public void setStepDistanceUnit(String stepDistanceUnit) {
        this.stepDistanceUnit = stepDistanceUnit;
    }

    public double getStepResolution() {
        return stepResolution;
    }

    public void setStepResolution(double stepResolution) {
        this.stepResolution = stepResolution;
    }
}
