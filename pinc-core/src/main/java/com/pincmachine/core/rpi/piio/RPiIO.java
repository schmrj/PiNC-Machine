/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pincmachine.core.rpi.piio;

import com.pi4j.gpio.extension.mcp.MCP3008GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3008Pin;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.spi.SpiChannel;

import java.io.IOException;
import java.util.TreeMap;

/**
 * 
 * @author Bobby
 */
public class RPiIO extends PIIO {

    private GpioController                          gpio         = null;
    protected TreeMap<String, GpioPinDigitalOutput> outputs      = new TreeMap<>();
    protected TreeMap<String, GpioPinDigitalInput>  inputs       = new TreeMap<>();
    protected TreeMap<String, GpioPinAnalogInput>   analogInputs = new TreeMap<>();

    public RPiIO() {
        gpio = GpioFactory.getInstance();
    }

    @Override
    public void addOutput(PinOut pin) {
        GpioPinDigitalOutput output = gpio.provisionDigitalOutputPin(
                pin.getPin(), pin.getName(), pin.getDefaultState());
        output.setState(pin.getShutdown());
        outputs.put(pin.getName(), output);
        output.setState(pin.getDefaultState());
    }

    @Override
    public void addInput(PinOut pin) {
        GpioPinDigitalInput input = gpio.provisionDigitalInputPin(pin.getPin());
        inputs.put(pin.getName(), input);
    }

    @Override
    public void addInput(PinOut pin, int debounce) {
        GpioPinDigitalInput input = gpio.provisionDigitalInputPin(pin.getPin());
        input.setDebounce(debounce);
        inputs.put(pin.getName(), input);
        
    }

    @Override
    public void addInput(PinOut pin, boolean isAnalog) throws IOException {
        if (isAnalog) {
        	GpioPinAnalogInput input = gpio.provisionAnalogInputPin(new MCP3008GpioProvider(SpiChannel.CS0), 
        			new MCP3008Pin().CH0, pin.getName());
            analogInputs.put(pin.getName(), input);
        }
        else {
            this.addInput(pin);
        }
    }
    
    public Double readAnalogInput(PinOut pin){
    	return readAnalogInput(pin.getName());
    }
    
    public Double readAnalogInput(String pinName){
    	GpioPinAnalogInput input = this.analogInputs.get(pinName);
    	
    	if(input != null){
    		double value = input.getValue();
    		return value;
    	}else
    		return null;
    }

    @Override
    public void addOutput(Pin pin, String name, PinState defaultState,
            PinState starting) {
        GpioPinDigitalOutput output = gpio.provisionDigitalOutputPin(pin, name,
                defaultState);
        output.setState(starting);
        outputs.put(name, output);
        output.setState(defaultState);
    }

    @Override
    public PinState getState(PinOut pin) {
        GpioPinDigitalInput iput = inputs.get(pin.getName());
        GpioPinDigitalOutput oput = outputs.get(pin.getName());

        if (iput != null) {
            PinState state = iput.getState();
            return state;
        }

        if (oput != null) {
            return oput.getState();
        }

        return null;
    }

    @Override
    public void setState(PinState state, PinOut out) {
        GpioPinDigitalOutput output = outputs.get(out.getName());

        if (output != null) {
            output.setState(state);
        }
        else {
            System.out.println("OUTPUT NOT FOUND: " + out.getName());
        }
    }

    @Override
    public void setState(PinState state, String name) {
        GpioPinDigitalOutput output = outputs.get(name);

        if (output != null) {
            output.setState(state);
        }
        else {
            System.out.println("OUTPUT NOT FOUND: " + name);
        }
    }

    @Override
    public void addDigitalListener(GpioPinListenerDigital listener, PinOut pin) {
        GpioPinDigitalInput input = inputs.get(pin.getName());

        if (input != null) {
            input.addListener(listener);
            MyLog.info("Listener added to input: " + pin.getName());
        }
        else {
        	MyLog.error("Could not find input, listener not added");
        }
    }
    
    public GpioPinDigitalMultipurpose provision(Pin pin, PinMode mode){
    	return gpio.provisionDigitalMultipurposePin(pin, mode);
    }
}
