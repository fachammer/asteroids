package com.fabianachammer.engine.input;

import com.badlogic.gdx.ai.msg.MessageDispatcher;

public interface InputSource {

	MessageDispatcher getMessageDispatcher();
	void update(float deltaTime);
}
