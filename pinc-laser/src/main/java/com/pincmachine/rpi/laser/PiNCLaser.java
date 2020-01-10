package com.pincmachine.rpi.laser;

import com.pincmachine.core.rpi.machine.MachineControl;
import com.pincmachine.core.rpi.piio.MockIO;
import com.pincmachine.core.rpi.piio.PIIO;
import com.pincmachine.rpi.laser.machine.GCodeInterpreter;
import com.pincmachine.rpi.laser.machine.PiNCLaserControl;
import com.pincmachine.rpi.laser.machine.config.LaserConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.pincmachine.rpi.laser")
public class PiNCLaser implements CommandLineRunner {

    @Autowired
    LaserConfig laserConfig;

    public static void main(String[] args) {
        SpringApplication.run(PiNCLaser.class, args);
    }

    @Bean
    public MachineControl getMachineControl() {
        return new PiNCLaserControl();
    }

    @Bean
    public GCodeInterpreter getInterpreter(){
        return new GCodeInterpreter();
    }

    @Bean
    public PIIO getPIIO(){
        return new MockIO();
    }

    @Override
    public void run(String[] args) {
        getMachineControl().startMachine();
    }
}
