package com.pincmachine.rpi.laser.machine.control;

import com.pincmachine.rpi.laser.machine.config.LaserConfig;
import org.springframework.beans.factory.annotation.Autowired;

public class AxisMath {

    @Autowired
    private LaserConfig laserConfig;

    public Double stepToAxisUnit(Double instructed){

        if(this.laserConfig.getMotorConfig().getStepDistanceUnit().equalsIgnoreCase("Inches")){
            return instructed * (this.laserConfig.getMotorConfig().getStepResolution() * 1.0);
        }

        return instructed;
    }

    public Double axisUnitToStep(Double instructed){
        if(this.laserConfig.getMotorConfig().getStepDistanceUnit().equalsIgnoreCase("Inches")){
            return instructed / (this.laserConfig.getMotorConfig().getStepResolution() * 1.0);
        }

        return instructed;
    }
}
