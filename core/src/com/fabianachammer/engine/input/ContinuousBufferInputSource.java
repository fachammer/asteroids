package com.fabianachammer.engine.input;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegram;
import com.fabianachammer.game.messages.InputMessage;

public class ContinuousBufferInputSource implements InputSource {

	private float currentValue;
	
	private final MessageDispatcher messageDispatcher = new MessageDispatcher();
	private final InputSource inputSource;
	
	public ContinuousBufferInputSource(InputSource inputSource) {
		this.inputSource = inputSource;
		inputSource.getMessageDispatcher().addListener(this::handleInputMessage, InputMessage.ID);
	}
	
	private boolean handleInputMessage(Telegram message){
		currentValue = ((InputMessage) message.extraInfo).getValue();
		return true;
	}

	@Override
	public MessageDispatcher getMessageDispatcher() {
		return messageDispatcher;
	}

	@Override
	public void update(float deltaTime) {
		inputSource.update(deltaTime);
		InputMessage messageToDispatch = InputMessage.obtain().setValue(currentValue).setInputSource(this);
		messageDispatcher.dispatchMessage(null, null, InputMessage.ID, messageToDispatch);
	}
}
