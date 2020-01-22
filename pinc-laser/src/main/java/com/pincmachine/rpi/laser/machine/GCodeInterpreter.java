package com.pincmachine.rpi.laser.machine;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pincmachine.core.rpi.machine.controllers.DRV8825;
import com.pincmachine.core.rpi.machine.controllers.DRV8825.ModeOption;
import com.pincmachine.core.rpi.piio.PIIO;
import com.pincmachine.core.rpi.piio.PinOut;
import com.pincmachine.rpi.laser.machine.config.LaserConfig;
import com.pincmachine.rpi.laser.machine.control.Axis;
import com.pincmachine.rpi.laser.machine.control.AxisBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class GCodeInterpreter {

    @Autowired
    LaserConfig laserConfig;

    @Autowired
    PIIO piio;

    private PinOut xAxisPin = null;
    private PinOut yAxisPin = null;
    private PinOut xAxisDirection = null;
    private PinOut yAxisDirection = null;

    private Double currentX = 0.0;
    private Double instructedX = null;
    private Double currentY = 0.0;
    private Double instructedY = null;
    private PinOut yEndStopPinOut;
    private PinOut xEndStopPinOut;
    private Double feedRate = 1000000.0;

    private PinOut stepModeM0 = null;
    private PinOut stepModeM1 = null;
    private PinOut stepModeM2 = null;

    public void init() throws Exception {

        if (!this.piio.hasPin("X-Axis")) {
            Pin xPin = RaspiPin.getPinByName("GPIO " + this.laserConfig.getMotorConfig().getxPin());
            this.xAxisPin = new PinOut("X-Axis", xPin, PinState.LOW, PinState.LOW);
            this.piio.addOutput(this.xAxisPin);
        }

        if (!this.piio.hasPin("Y-Axis")) {
            Pin yPin = RaspiPin.getPinByName("GPIO " + this.laserConfig.getMotorConfig().getyPin());
            this.yAxisPin = new PinOut("Y-Axis", yPin, PinState.LOW, PinState.LOW);
            this.piio.addOutput(this.yAxisPin);
        }

        if (!this.piio.hasPin("X-Dir-Pin")) {
            Pin xDirectionPin = RaspiPin.getPinByName("GPIO " + this.laserConfig.getMotorConfig().getxPinDir());
            this.xAxisDirection = new PinOut("X-Dir-Pin", xDirectionPin, PinState.LOW, PinState.LOW);
            this.piio.addOutput(this.xAxisDirection);
        }

        if (!this.piio.hasPin("Y-Dir-Pin")) {
            Pin yDirectionPin = RaspiPin.getPinByName("GPIO " + this.laserConfig.getMotorConfig().getyPinDir());
            this.yAxisDirection = new PinOut("Y-Dir-Pin", yDirectionPin, PinState.LOW, PinState.LOW);
            this.piio.addOutput(this.yAxisDirection);
        }

        if (!this.piio.hasPin("X-EndStop-Pin")) {
            Pin xEndStopPin = RaspiPin.getPinByName("GPIO " + this.laserConfig.getMotorConfig().getxPinEndstop());
            this.xEndStopPinOut = new PinOut("X-EndStop-Pin", xEndStopPin, PinState.LOW, PinState.LOW);
            this.piio.addInput(xEndStopPinOut, false);
        }

        if (!this.piio.hasPin("Y-EndStop-Pin")) {
            Pin yEndStopPin = RaspiPin.getPinByName("GPIO " + this.laserConfig.getMotorConfig().getyPinEndstop());
            this.yEndStopPinOut = new PinOut("Y-EndStop-Pin", yEndStopPin, PinState.LOW, PinState.LOW);
            this.piio.addInput(yEndStopPinOut, false);
        }

        if (!this.piio.hasPin("STEP-MODE-0")) {
            Pin stepPinM0 = RaspiPin.getPinByName("GPIO " + this.laserConfig.getMotorConfig().getStepM0Pin());
            this.stepModeM0 = new PinOut("STEP-MODE-0", stepPinM0, PinState.LOW, PinState.LOW);
            this.piio.addOutput(stepModeM0);
        }

        if (!this.piio.hasPin("STEP-MODE-1")) {
            Pin stepPinM1 = RaspiPin.getPinByName("GPIO " + this.laserConfig.getMotorConfig().getStepM1Pin());
            this.stepModeM1 = new PinOut("STEP-MODE-1", stepPinM1, PinState.LOW, PinState.LOW);
            this.piio.addOutput(stepModeM1);
        }

        if (!this.piio.hasPin("STEP-MODE-2")) {
            Pin stepPinM2 = RaspiPin.getPinByName("GPIO " + this.laserConfig.getMotorConfig().getStepM2Pin());
            this.stepModeM2 = new PinOut("STEP-MODE-2", stepPinM2, PinState.LOW, PinState.LOW);
            this.piio.addOutput(stepModeM2);
        }

        this.setMode();
    }

    public void setMode(){
        String sMode = this.laserConfig.getMotorConfig().getStepMode();
        ModeOption mode = DRV8825.getMode(sMode);
        System.out.println("Selecting Mode: " + mode.getModeName());

        // Set M0
        this.piio.setState(mode.getM0(), this.stepModeM0);

        // Set M1
        this.piio.setState(mode.getM1(), this.stepModeM1);

        // Set M2
        this.piio.setState(mode.getM2(), this.stepModeM2);
    }

    public void interpret(String command) throws Exception {
        System.out.println("Executing: " + command);
        this.processCode(command.split(" "));
    }

    private void processCode(String[] commands) throws Exception {

        for (String command : commands) {
            if (command != null && !command.isEmpty()) {
                this.init();
                command = command.toUpperCase();
                char first = command.charAt(0);

                switch (first) {
                    case 'X':
                        this.xCode(command);
                        break;
                    case 'Y':
                        this.yCode(command);
                        break;
                    case 'F':
                        this.fCode(command);
                        break;
                    case 'G':
                        this.gCode(command);
                        break;
                    default:
                        break;
                }
            }
        }

        if (commands != null && commands.length > 0) {
            this.execute();
        }
    }

    private void gCode(String command) {
        String value = command.substring(1);
        Integer gValue = Integer.parseInt(value);

        // TODO Setup G Code Commands

        switch (gValue) {
            case 20:
                System.out.println("Setting units to: INCHES");
                this.laserConfig.getMotorConfig().setStepDistanceUnit("inches");
                break;
            case 21:
                System.out.println("Setting units to: MM");
                this.laserConfig.getMotorConfig().setStepDistanceUnit("mm");
                break;
            case 28:
                System.out.println("AUTO HOME ALL AXIS");
                this.findRailLimits();
                break;
            default:
                System.out.println("Not Implemented");
                break;
        }
    }

    private void findRailLimits() {
        CyclicBarrier barrier2 = new CyclicBarrier(1);
        Thread xHome = new Thread() {

            public void run() {
                try {
                    barrier2.await();

                    // Find the endstop
                    homeAxis(xAxisPin, xAxisDirection, xEndStopPinOut, piio, (Integer.MIN_VALUE + 1.0), false);
                    // Add buffer of 10 steps
                    homeAxis(xAxisPin, xAxisDirection, xEndStopPinOut, piio, 100.0, true);

                    // Find Opposite Endstop
                    Double length = homeAxis(xAxisPin, xAxisDirection, xEndStopPinOut, piio, Integer.MAX_VALUE - 1.0, false);
                    laserConfig.getWorkspace().setxSize(length + 50.0);
                    // Move to Center
                    homeAxis(xAxisPin, xAxisDirection, xEndStopPinOut, piio, (length * -1.0), true);
                    // Move to beginning

                    currentX = (length / 2.0) * -1.0;
                    System.out.println("Max X Axis Steps: " + (length / 2));
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread yHome = new Thread() {
            public void run() {
                try {
                    barrier2.await();
                    // Find the endstop
                    homeAxis(yAxisPin, yAxisDirection, yEndStopPinOut, piio, (Integer.MIN_VALUE + 1.0), false);
                    // Add buffer of 10 steps
                    homeAxis(yAxisPin, yAxisDirection, yEndStopPinOut, piio, 100.0, true);

                    // Find Opposite Endstop
                    Double length = homeAxis(yAxisPin, yAxisDirection, yEndStopPinOut, piio, Integer.MAX_VALUE - 1.0, false);
                    laserConfig.getWorkspace().setySize(length + 50.0);
                    // Move to Center
                    homeAxis(yAxisPin, yAxisDirection, yEndStopPinOut, piio, (length * -1.0), true);
                    // Move to beginning

                    System.out.println("Max Y Axis Steps: " + (length / 2));

                    currentY = (length / 2.0) * -1.0;
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        };

//        yHome.start();
        xHome.start();

    }

    private Double homeAxis(PinOut axisPin, PinOut directionPin, PinOut endstop, PIIO piio, Double targetDestination, boolean ignoreEndStop) {
        CyclicBarrier barrier = new CyclicBarrier(1);
        Axis axis = AxisBuilder.setAxisPin(axisPin, piio, Integer.MAX_VALUE * 1.0)
                .setDirectionPin(directionPin)
                .setEndStopPin(endstop)
                .setIgnoreLimits(ignoreEndStop)
                .setBarrier(barrier)
                .setCurrentPosition(0.0)
                .setMove(targetDestination)
                .setFeedRate(this.feedRate)
                .build();
        axis.start();

        while (!axis.isComplete()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return axis.getFinalDestination();
    }

    private void xCode(String command) {
        String value = command.substring(1);
        Double xValue = Double.parseDouble(value);

        System.out.println("Setting X to: " + xValue);
        this.instructedX = xValue;
    }

    private void fCode(String command) {
        String value = command.substring(1);
        Double feed = Double.parseDouble(value);

        System.out.println("Setting FeedRate to: " + feed);
        this.feedRate = feed;
    }

    private void yCode(String command) {
        String value = command.substring(1);
        Double yValue = Double.parseDouble(value);

        System.out.println("Setting Y to: " + yValue);
        this.instructedY = yValue;
    }

    private Double getDistance(Double start, Double end) {

        if (start != null && end != null) {
            Double diff = start - end;
            if (diff < 0)
                diff = diff * -1.0;

            return diff;
        }
        return null;
    }

    private void execute() {
        CyclicBarrier barrier = new CyclicBarrier(2);

        Axis xAxis = null;
        Axis yAxis = null;

        if (this.instructedX == null)
            this.instructedX = this.currentX;
        if (this.instructedY == null)
            this.instructedY = this.currentY;

        Double xDistance = this.getDistance(this.currentX, this.instructedX);
        Double yDistance = this.getDistance(this.currentY, this.instructedY);



        Double feedRateX = this.feedRate;
        Double feedRateY = this.feedRate;

        // Build X Axis
        xAxis = AxisBuilder.setAxisPin(this.xAxisPin, this.piio, this.laserConfig.getWorkspace().getxSize())
                .setDirectionPin(this.xAxisDirection)
                .setEndStopPin(this.xEndStopPinOut)
                .setIgnoreLimits(false)
                .setBarrier(barrier)
                .setCurrentPosition(this.currentX)
                .setMove(this.instructedX)
                .setFeedRate(feedRateX)
                .build();

        xAxis.start();

        // Build Y Axis
        yAxis = AxisBuilder.setAxisPin(this.yAxisPin, this.piio, this.laserConfig.getWorkspace().getySize())
                .setDirectionPin(this.yAxisDirection)
                .setEndStopPin(this.yEndStopPinOut)
                .setIgnoreLimits(false)
                .setBarrier(barrier)
                .setCurrentPosition(this.currentY)
                .setMove(this.instructedY)
                .setFeedRate(feedRateY)
                .build();

        yAxis.start();

        boolean complete = false;
        do {
            if ((xAxis != null && !xAxis.isComplete()) || (yAxis != null && !yAxis.isComplete())) {
                complete = false;
            } else
                complete = true;

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!complete);
        // Finished with the execution

        if (xAxis != null) {
            this.currentX = this.instructedX;
            this.instructedX = null;
        }

        if (yAxis != null) {
            this.currentY = this.instructedY;
            this.instructedY = null;
        }
    }
}
