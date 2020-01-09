package com.pincmachine.rpi.laser.machine.config;

import org.springframework.stereotype.Component;

@Component
public class Workspace {

    private int xSize;
    private int ySize;

    public int getxSize() {
        return xSize;
    }

    public void setxSize(int xSize) {
        this.xSize = xSize;
    }

    public int getySize() {
        return ySize;
    }

    public void setySize(int ySize) {
        this.ySize = ySize;
    }
}
