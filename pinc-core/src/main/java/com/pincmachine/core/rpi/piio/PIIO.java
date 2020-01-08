package com.pincmachine.core.rpi.piio;

import java.io.IOException;
import java.util.TreeMap;

import com.pi4j.io.gpio.GpioPinDigitalMultipurpose;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.apache.log4j.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 
 * @author Bobby
 */
public abstract class PIIO {

    public static final Logger MyLog = Logger.getLogger("PIIO");
    protected TreeMap<String, PinOut> pins = new TreeMap<String, PinOut>();

    public PIIO() {

    }

    public abstract void addOutput(PinOut pin) throws Exception;

    public abstract void addInput(PinOut pin);

    public abstract void addInput(PinOut pin, int debounce);

    public abstract void addInput(PinOut pin, boolean isAnalog) throws IOException;

    public abstract void addOutput(Pin pin, String name, PinState defaultState,
            PinState starting);

    public abstract PinState getState(PinOut pin);

    public abstract void setState(PinState state, PinOut out);

    public abstract void setState(PinState state, String name);

    public abstract void addDigitalListener(GpioPinListenerDigital listener,
            PinOut pin);
    
    public abstract Double readAnalogInput(String pinName);
    
    public abstract Double readAnalogInput(PinOut pin);
    
    public abstract GpioPinDigitalMultipurpose provision(Pin pin, PinMode mode);
}
