package com.pincmachine.rpi.laser.machine.control;

import com.pi4j.io.gpio.PinState;
import com.pincmachine.core.rpi.piio.PIIO;
import com.pincmachine.core.rpi.piio.PinOut;

import java.util.concurrent.CyclicBarrier;

public class AxisBuilder {

    private AxisBuilder() {

    }

    public static SetDirectionPin setAxisPin(PinOut axisPin, PIIO piio, Integer axisLength) {
        return new Builder().setAxisPin(axisPin, piio, axisLength);
    }

    public static interface SetAxisPin {
        SetDirectionPin setAxisPin(PinOut axisPin, PIIO piio, Integer axisLength);
    }

    public static interface SetDirectionPin {
        SetEndStopPin setDirectionPin(PinOut directionPin);
    }

    public static interface SetEndStopPin{
        SetBarrier setEndStopPin(PinOut endStopPin);
    }

    public static interface SetBarrier{
        SetCurrentPosition setBarrier(CyclicBarrier barrier);
    }

//    public static interface SetDirection {
//        SetCurrentPosition setDirection(Integer direction);
//    }

    public static interface SetCurrentPosition{
        SetMove setCurrentPosition(Integer currentStep);
    }

    public static interface SetMove{
        SetFeedRate setMove(Integer newPosition);
    }

    public static interface SetFeedRate{
        Build setFeedRate(Integer feedRate);
    }

    public static interface Build{
        Axis build();
    }

    static class Builder implements SetAxisPin, SetDirectionPin, SetEndStopPin, SetBarrier, SetCurrentPosition
                                    ,SetMove, SetFeedRate, Build {
        private PinOut axisPin = null;
        private PIIO piio = null;
        private PinOut directionPin = null;
        private PinOut endStop = null;
        private PinState direction = null;

        private CyclicBarrier barrier = null;

        private Integer currentPosition = null;
        private Integer instructedPosition = null;
        private Integer feedRate = null;
        private Integer axisLength = null;

        public SetDirectionPin setAxisPin(PinOut axisPin, PIIO piio, Integer axisLength) {
            this.piio = piio;
            this.axisPin = axisPin;
            this.axisLength = axisLength;
            return this;
        }

        @Override
        public SetEndStopPin setDirectionPin(PinOut directionPin) {
            this.directionPin = directionPin;
            return this;
        }

        @Override
        public SetBarrier setEndStopPin(PinOut endStopPin) {
            this.endStop = endStopPin;
            return this;
        }

        @Override
        public SetCurrentPosition setBarrier(CyclicBarrier barrier) {
            this.barrier = barrier;
            return this;
        }



        @Override
        public SetMove setCurrentPosition(Integer currentStep) {
            this.currentPosition = currentStep;
            return this;
        }

        @Override
        public SetFeedRate setMove(Integer newPosition) {
            this.instructedPosition = newPosition;
            return this;
        }

        public Build setFeedRate(Integer feedRate){
            this.feedRate = feedRate;
            return this;
        }

        @Override
        public Axis build() {

            Axis axis = new Axis();
            axis.setAxisPin(this.axisPin);
            axis.setDirectionPin(this.directionPin);
            axis.setEndStopPin(this.endStop);
            axis.setBarrier(this.barrier);
            axis.setCurrentPosition(this.currentPosition);
            axis.setInstructedPosition(this.instructedPosition);
            axis.setFeedRate(this.feedRate);
            axis.setPiio(this.piio);
            axis.setAxisLength(this.axisLength);

            return axis;
        }
    }

}