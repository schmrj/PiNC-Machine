package com.pincmachine.rpi.laser.machine.control;

import com.pi4j.io.gpio.PinState;
import com.pincmachine.core.rpi.piio.PIIO;
import com.pincmachine.core.rpi.piio.PinOut;
import com.pincmachine.rpi.laser.machine.config.MotorConfig;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Axis extends Thread {

    private boolean complete = false;
    private boolean ignoreLimits = false;
    private CyclicBarrier barrier = null;

    private Integer axisLength = null;
    private Integer currentPosition;
    private Integer instructedPosition;
    private Integer finalDestination = null;
    private PIIO piio;
    private PinOut axisPin;
    private PinOut directionPin = null;
    private PinOut endStopPin = null;
    private PinState direction = null;
    private Integer feedRate = null;

    private boolean atEndstop = false;

    private Integer calculateDistance() {
        Integer distance = this.currentPosition - this.instructedPosition;
        if (distance < 0) {
            distance = distance * -1;
        }

        if (currentPosition < this.instructedPosition) {
            this.direction = MotorConfig.CLOCKWISE;
        } else {
            this.direction = MotorConfig.COUNTER_CLOCKWISE;
        }
        return distance;
    }

    public void run() {
        try {

            this.direction = MotorConfig.CLOCKWISE;
            Integer distance = this.calculateDistance();
            this.barrier.await();

            for (int i = 0; i < distance; i++) {
                this.piio.setState(this.direction, this.directionPin);

                Integer nextPosition = null;
                if (this.direction == MotorConfig.CLOCKWISE) {
                    nextPosition = this.currentPosition + i;
                } else {
                    nextPosition = this.currentPosition - i;
                }

                if (!this.checkEndStop(nextPosition)) {
                    this.piio.setState(PinState.HIGH, this.axisPin);
                    long startTime = System.nanoTime();
                    Long end = null;
                    do {
                        end = System.nanoTime();
                    } while (startTime + this.feedRate > end);
                    this.piio.setState(PinState.LOW, this.axisPin);
                    this.finalDestination = nextPosition;
                } else {
                    break;
                }
            }

            if(this.finalDestination == null)
                this.finalDestination = this.currentPosition;

            System.out.println("Instructed Position: " + this.instructedPosition + ", Reached Position: " + this.finalDestination);

            this.complete = true;
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }


    public boolean checkEndStop(Integer nextPosition) {
        boolean blockMove = false;

        PinState state = this.piio.getState(this.endStopPin);

        if(state == PinState.HIGH && this.ignoreLimits)
            return false;

        if (state == PinState.HIGH)
            blockMove = true;

        if (!this.ignoreLimits) {
            System.out.println("Axis Length: " + this.axisLength);
            Integer diff = this.axisLength / 2;
            if (nextPosition < (diff * -1) || nextPosition > (diff))
                blockMove = true;
        }


        return blockMove;
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

    public Integer getAxisLength() {
        return axisLength;
    }

    public void setAxisLength(Integer axisLength) {
        this.axisLength = axisLength;
    }

    public Integer getFinalDestination() {
        return finalDestination;
    }

    public boolean isIgnoreLimits() {
        return ignoreLimits;
    }

    public void setIgnoreLimits(boolean ignoreLimits) {
        this.ignoreLimits = ignoreLimits;
    }

}
