package com.pincmachine.core.rpi.piio;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

public class PinOut {

	private String name = null;
	private Pin pin = null;
	private PinState defaultState = null;
	private PinState shutdown = null;

	public PinOut(String name, Pin pin, PinState defaultState, PinState shutdown) {
		this.name = name;
		this.pin = pin;
		this.defaultState = defaultState;
		this.shutdown = shutdown;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Pin getPin() {
		return pin;
	}

	public void setPin(Pin pin) {
		this.pin = pin;
	}

	public PinState getDefaultState() {
		return defaultState;
	}

	public void setDefaultState(PinState defaultState) {
		this.defaultState = defaultState;
	}

	public PinState getShutdown() {
		return shutdown;
	}

	public void setShutdown(PinState shutdown) {
		this.shutdown = shutdown;
	}

}
