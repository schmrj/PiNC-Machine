package com.pincmachine.rpi.laser.machine.control;

public class Axis extends Thread {

    private Integer instructedPosition = null;

    private boolean complete = false;

    public void run() {


        this.complete = true;
    }


}
