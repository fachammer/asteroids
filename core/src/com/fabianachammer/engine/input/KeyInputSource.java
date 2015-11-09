package com.fabianachammer.engine.input;

import com.badlogic.gdx.ai.msg.MessageDispatcher;

public class KeyInputSource implements InputSource {

	private final MessageDispatcher messageDispatcher = new MessageDispatcher();
	
	@Override
	public MessageDispatcher getMessageDispatcher() {
		return messageDispatcher;
	}

	@Override
	public void update(float deltaTime) {
	}
}
