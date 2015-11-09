package com.fabianachammer.engine.input;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegram;
import com.fabianachammer.game.messages.InputMessage;

public class SmoothedInputSource implements InputSource {

	private final MessageDispatcher messageDispatcher = new MessageDispatcher();
	private final InputSource digitalInputSource;
	
	private float targetValue;
	private float currentValue;
	private float inputSpeed;
	
	private static final float INPUT_EPSILON = 0.01f;
	
	public SmoothedInputSource(float smoothingSpeed, InputSource digitalInputSource) {
		this.inputSpeed = smoothingSpeed;
		this.digitalInputSource = digitalInputSource;
		digitalInputSource.getMessageDispatcher().addListener(this::handleInputMessage, InputMessage.ID);
	}
	
	private boolean handleInputMessage(Telegram message){
		InputMessage input = (InputMessage) message.extraInfo;
		targetValue = input.getValue();
		return true;
	}

	@Override
	public MessageDispatcher getMessageDispatcher() {
		return messageDispatcher;
	}

	@Override
	public void update(float deltaTime) {
		digitalInputSource.update(deltaTime);
		
		if(targetValue == currentValue)
			return;
		
		currentValue = calculateNewInputValue(deltaTime);
		
		InputMessage messageToDispatch = InputMessage.obtain()
				.setValue(currentValue)
				.setInputSource(this);
		messageDispatcher.dispatchMessage(null, InputMessage.ID,
				messageToDispatch);
	}

	private float calculateNewInputValue(float deltaTime) {
		if(isInputAlmostAtTarget())
			return targetValue;
		else
			return currentValue + Math.signum(targetValue - currentValue) * inputSpeed * deltaTime;
	}

	private boolean isInputAlmostAtTarget() {
		// multiply with input speed to make INPUT_EPSILON work as a percentage
		return Math.abs(targetValue - currentValue) < INPUT_EPSILON * inputSpeed / 2;
	}

}
