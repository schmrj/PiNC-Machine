package com.pincmachine.core.rpi.machine.controllers;


import com.pi4j.io.gpio.PinState;

import static com.pi4j.io.gpio.PinState.HIGH;
import static com.pi4j.io.gpio.PinState.LOW;

public class DRV8825 {

    PinState mo = LOW;
    PinState m1 = LOW;
    PinState m2 = LOW;

    public static ModeOption getMode(String name) {
        ModeOption[] options = ModeOption.values();

        if (name != null) {
            for (ModeOption option : options) {
                if(option.getModeName().equalsIgnoreCase(name))
                    return option;
            }
        }

        return null;
    }

    public enum ModeOption {

        STEP_1("FULL", LOW, LOW, LOW),
        STEP_2("HALF", HIGH, LOW, LOW),
        STEP_4("QUARTER", LOW, HIGH, LOW),
        STEP_8("8TH", HIGH, HIGH, LOW),
        STEP_16("16TH", LOW, LOW, HIGH),
        STEP_32("32ND", HIGH, LOW, HIGH);

        private String modeName = null;
        private PinState M0 = null;
        private PinState M1 = null;
        private PinState M2 = null;

        ModeOption(String modeName, PinState M0, PinState M1, PinState M2) {
            this.modeName = modeName;
            this.M0 = M0;
            this.M1 = M1;
            this.M2 = M2;
        }

        public String getModeName() {
            return modeName;
        }

        public PinState getM0() {
            return M0;
        }

        public PinState getM1() {
            return M1;
        }

        public PinState getM2() {
            return M2;
        }
    }
}
