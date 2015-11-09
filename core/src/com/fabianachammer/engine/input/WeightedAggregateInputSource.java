package com.fabianachammer.engine.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegram;
import com.fabianachammer.game.messages.InputMessage;

public class WeightedAggregateInputSource implements InputSource {

	private final Map<InputSource, Float> inputSourceWeights = new HashMap<InputSource, Float>();

	private final MessageDispatcher messageDispatcher = new MessageDispatcher();
	private final ArrayList<InputMessage> inputMessages = new ArrayList<InputMessage>();

	@Override
	public MessageDispatcher getMessageDispatcher() {
		return messageDispatcher;
	}

	public WeightedAggregateInputSource addInputSource(InputSource source, float weight) {
		inputSourceWeights.put(source, weight);
		source.getMessageDispatcher().addListener(this::handleInputMessage,
				InputMessage.ID);
		return this;
	}

	private boolean handleInputMessage(Telegram message) {
		inputMessages.add((InputMessage) message.extraInfo);
		return true;
	}

	@Override
	public void update(float deltaTime) {
		updateInputSources(deltaTime);
		if (inputMessages.isEmpty())
			return;

		inputMessages.stream().map(this::weightedInput)
				.reduce((valueA, valueB) -> valueA + valueB)
				.ifPresent(this::dispatchInputMessage);

		inputMessages.clear();
	}

	private float weightedInput(InputMessage input) {
		return input.getValue()
				* inputSourceWeights.get(input.getInputSource());
	}

	private void dispatchInputMessage(float inputValue) {
		InputMessage dispatchInput = InputMessage.obtain().setValue(inputValue)
				.setInputSource(this);
		messageDispatcher.dispatchMessage(null, null, InputMessage.ID,
				dispatchInput);
	}

	private void updateInputSources(float deltaTime) {
		inputSourceWeights.keySet().forEach(input -> input.update(deltaTime));
	}
}
