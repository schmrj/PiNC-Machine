package com.pincmachine.rpi.laser;

import com.pincmachine.core.rpi.machine.MachineControl;
import com.pincmachine.rpi.laser.machine.PiNCLaserControl;
import com.pincmachine.rpi.laser.machine.config.LaserConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PiNCLaser implements CommandLineRunner {

    @Autowired
    LaserConfig laserConfig;

    public static void main(String [] args){
        SpringApplication.run(PiNCLaser.class, args);
    }

    @Bean
    public MachineControl getMachineControl(){
        return new PiNCLaserControl();
    }

    @Override
    public void run(String [] args){
        getMachineControl().startMachine();
    }
}
