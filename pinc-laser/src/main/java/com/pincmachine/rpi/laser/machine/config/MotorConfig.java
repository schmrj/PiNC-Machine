package com.pincmachine.rpi.laser.machine.config;

import org.springframework.stereotype.Component;

@Component
public class MotorConfig {

    private int xPin;
    private int xPinDir;
    private int xPinEndstop;

    private int yPin;
    private int yPinDir;
    private int yPinEndstop;

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
}
