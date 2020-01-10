package com.pincmachine.rpi.laser.machine;

import com.pincmachine.core.rpi.machine.MachineControl;
import com.pincmachine.rpi.laser.machine.config.LaserConfig;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Scanner;

public class PiNCLaserControl implements MachineControl {

    @Autowired
    GCodeInterpreter interpreter;

    @Autowired
    LaserConfig config;

    @Override
    public void startMachine() {
        Scanner scan = new Scanner(System.in);

        do {
            System.out.print("Enter machine command (G-Code): ");
            String command = scan.nextLine();

            try {
                interpreter.interpret(command);
            }catch(Exception e){
                e.printStackTrace();
            }
        }while(true);
    }
}
