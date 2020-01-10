package com.pincmachine.rpi.laser.machine.control;

import com.pi4j.io.gpio.PinState;
import com.pincmachine.core.rpi.piio.PIIO;
import com.pincmachine.core.rpi.piio.PinOut;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Axis extends Thread {

    private boolean complete = false;
    private CyclicBarrier barrier = null;

    private Integer currentPosition;
    private Integer instructedPosition;
    private PIIO piio;
    private PinOut axisPin;
    private PinOut directionPin = null;
    private PinOut endStopPin = null;
    private PinState direction = null;
    private Integer feedRate = null;


    public void run() {
        try {
            this.barrier.await();

            for(int i = 0; i < 10; i++){

            }


            this.complete = true;
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }


    public boolean isComplete() {
        return complete;
    }

    public Integer getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Integer currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Integer getInstructedPosition() {
        return instructedPosition;
    }

    public void setInstructedPosition(Integer instructedPosition) {
        this.instructedPosition = instructedPosition;
    }

    public PIIO getPiio() {
        return piio;
    }

    public void setPiio(PIIO piio) {
        this.piio = piio;
    }

    public PinOut getAxisPin() {
        return axisPin;
    }

    public void setAxisPin(PinOut axisPin) {
        this.axisPin = axisPin;
    }

    public PinState getDirection() {
        return direction;
    }

    public void setDirection(PinState direction) {
        this.direction = direction;
    }

    public PinOut getDirectionPin() {
        return directionPin;
    }

    public void setDirectionPin(PinOut directionPin) {
        this.directionPin = directionPin;
    }

    public PinOut getEndStopPin() {
        return endStopPin;
    }

    public void setEndStopPin(PinOut endStopPin) {
        this.endStopPin = endStopPin;
    }

    public CyclicBarrier getBarrier() {
        return barrier;
    }

    public void setBarrier(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    public Integer getFeedRate() {
        return feedRate;
    }

    public void setFeedRate(Integer feedRate) {
        this.feedRate = feedRate;
    }
}
