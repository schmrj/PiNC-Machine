package com.pincmachine.rpi.laser.machine;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
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

    private Integer currentX = 0;
    private Integer instructedX = null;
    private Integer currentY = 0;
    private Integer instructedY = null;
    private PinOut yEndStopPinOut;
    private PinOut xEndStopPinOut;

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
            case 28:
                System.out.println("AUTO HOME ALL AXIS");
                this.currentY = (laserConfig.getWorkspace().getySize() / 2) * -1;
                this.currentX = (laserConfig.getWorkspace().getxSize() / 2) * -1;
                break;
            default:
                System.out.println("Not Implemented");
                break;
        }
    }

    private void xCode(String command) {
        String value = command.substring(1);
        Integer xValue = Integer.parseInt(value);

        System.out.println("Setting X to: " + xValue);
        this.instructedX = xValue;
    }

    private void yCode(String command) {
        String value = command.substring(1);
        Integer yValue = Integer.parseInt(value);

        System.out.println("Setting Y to: " + yValue);
        this.instructedY = yValue;
    }

    private Integer getDistance(Integer start, Integer end) {

        if (start != null && end != null) {
            Integer diff = start - end;
            if(diff < 0)
                diff = diff * -1;

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

        Integer xDistance = this.getDistance(this.currentX, this.instructedX);
        Integer yDistance = this.getDistance(this.currentY, this.instructedY);
        Integer feedRateX = 10;
        Integer feedRateY = 10;

        // Build X Axis
        xAxis = AxisBuilder.setAxisPin(this.xAxisPin, this.piio, this.laserConfig.getWorkspace().getxSize())
                .setDirectionPin(this.xAxisDirection)
                .setEndStopPin(this.xEndStopPinOut)
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
