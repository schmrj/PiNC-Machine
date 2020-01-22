package com.pincmachine.rpi.laser.machine.config;

import org.springframework.stereotype.Component;

@Component
public class Workspace {

    private Double xSize;
    private Double ySize;

    public Double getxSize() {
        return xSize;
    }

    public void setxSize(Double xSize) {
        this.xSize = xSize;
    }

    public Double getySize() {
        return ySize;
    }

    public void setySize(Double ySize) {
        this.ySize = ySize;
    }
}
