package com.pincmachine.core.rpi.piio;

import java.io.InputStream;
import java.util.Properties;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class IOProperties extends ConfigProperties {

	private String filename = null;
	private InputStream is = null;
	private Properties props = new Properties();

	public IOProperties(String fileName) throws Exception {
		super(fileName);
	}

	public Pin getPin(String name) {
		return RaspiPin.getPinByName("GPIO " + name);
	}

	public PinState getState(String name) {
		String state = getProperty(name);

		if (state != null) {
			if (state.toUpperCase().equals("OFF"))
				return PinState.LOW;
			else if (state.toUpperCase().equals("ON"))
				return PinState.HIGH;
		}

		return null;
	}
}
