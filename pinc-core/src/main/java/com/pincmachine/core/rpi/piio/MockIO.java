package com.pincmachine.core.rpi.piio;

import java.util.TreeMap;

import com.pi4j.io.gpio.GpioPinDigitalMultipurpose;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class MockIO extends PIIO {

	private TreeMap<String, MockInput> inputs = new TreeMap<>();
	private TreeMap<String, MockOutput> outputs = new TreeMap<>();

	@Override
	public void addOutput(PinOut pin) throws Exception {
		if (pins.get(pin.getName()) == null) {
			MockOutput out = new MockOutput();
			outputs.put(pin.getName(), out);
			pins.put(pin.getName(), pin);
		} else {
			throw new Exception("Pin Already Assigned");
		}
	}

	@Override
	public void addInput(PinOut pin) {
		MockInput input = new MockInput();
		input.setPin(pin);
		inputs.put(pin.getName(), input);
	}

	@Override
	public void addInput(PinOut pin, int debounce) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addInput(PinOut pin, boolean isAnalog) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addOutput(Pin pin, String name, PinState defaultState, PinState starting) {
		// TODO Auto-generated method stub

	}

	@Override
	public PinState getState(PinOut input) {
		MockOutput out = outputs.get(input.getName());
		MockInput in = inputs.get(input.getName());

		if (out != null)
			return out.getState();
		if (in != null)
			return in.getState();

		return null;
	}

	@Override
	public void setState(PinState state, PinOut out) {
		setState(state, out.getName());
	}

	@Override
	public void setState(PinState state, String name) {
		MockOutput output = outputs.get(name);

		if (output != null) {
			MyLog.debug(name + " state: " + state.getName());
			output.setState(state);
		} else {
			MyLog.error("Could not find output: " + name);
		}
	}

	public class MockOutput {

		private PinOut pin = null;
		private PinState state = PinState.LOW;

		public PinState getState() {
			return state;
		}

		public void setState(PinState state) {
			this.state = state;
		}

		public PinOut getPin() {
			return pin;
		}

		public void setPin(PinOut pin) {
			this.pin = pin;
		}
	}

	public class MockInput extends MockOutput {
	}

	@Override
	public void addDigitalListener(GpioPinListenerDigital listener, PinOut pin) {
		MockInput input = inputs.get(pin.getName());

		if (input != null) {
			MyLog.info("Listener added to input: " + pin.getName());
		} else {
			MyLog.error("Could not find input, listener not added");
		}
	}

	@Override
	public Double readAnalogInput(PinOut pin) {
		return readAnalogInput(pin.getName());
	}

	@Override
	public Double readAnalogInput(String pinName) {
		// TODO Auto-generated method stub
		MyLog.debug("ANALOG INPUT, MOCK RETURN: 512");
		return 512.0;
	}

	@Override
	public GpioPinDigitalMultipurpose provision(Pin pin, PinMode mode) {
		// TODO Auto-generated method stub
		return null;
	}
}
