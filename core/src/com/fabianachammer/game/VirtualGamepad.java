package com.fabianachammer.game;

import com.badlogic.gdx.Input.Keys;
import com.fabianachammer.engine.input.ContinuousBufferInputSource;
import com.fabianachammer.engine.input.InputSource;
import com.fabianachammer.engine.input.Keyboard;
import com.fabianachammer.engine.input.SmoothedInputSource;
import com.fabianachammer.engine.input.WeightedAggregateInputSource;

public class VirtualGamepad {
	
	private InputSource rotationInput;
	private InputSource thrustInput;
	private InputSource shootInput;
	
	public VirtualGamepad(Keyboard keyboard){
		rotationInput = createRotationInput(keyboard);
		thrustInput = createThrustInput(keyboard);
		shootInput = createShootInput(keyboard);
	}

	public InputSource getRotationInput(){
		return rotationInput;
	}
	
	public InputSource getThrustInput(){
		return thrustInput;
	}
	
	public InputSource getShootInput(){
		return shootInput;
	}
	
	private static InputSource createShootInput(Keyboard keyboard) {
		return new ContinuousBufferInputSource(new WeightedAggregateInputSource()
			.addInputSource(keyboard.getKeyInputSource(Keys.SPACE), 1));
	}

	private static InputSource createThrustInput(Keyboard keyboard) {
		return new ContinuousBufferInputSource(new WeightedAggregateInputSource()
		.addInputSource(new SmoothedInputSource(10f, keyboard.getKeyInputSource(Keys.W)), 1f));
	}

	private static InputSource createRotationInput(Keyboard keyboard) {
		return new ContinuousBufferInputSource(new WeightedAggregateInputSource()
				.addInputSource(new SmoothedInputSource(10f, keyboard.getKeyInputSource(Keys.A)), 1f)
				.addInputSource(new SmoothedInputSource(10f, keyboard.getKeyInputSource(Keys.D)), -1f));
	}
}
