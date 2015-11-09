package com.fabianachammer.engine.input;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;
import com.fabianachammer.game.messages.InputMessage;

public class Keyboard extends InputAdapter implements Disposable {

	private final IntMap<InputSource> keys = new IntMap<InputSource>();

	public InputSource getKeyInputSource(int keyCode) {
		if (!keys.containsKey(keyCode))
			keys.put(keyCode, new KeyInputSource());

		return keys.get(keyCode);
	}

	@Override
	public boolean keyDown(int keycode) {
		if (!keys.containsKey(keycode))
			return true;

		InputSource inputSource = keys.get(keycode);
		InputMessage messageToDispatch = InputMessage.obtain().setValue(1f)
				.setInputSource(inputSource);

		inputSource.getMessageDispatcher().dispatchMessage(null, null,
				InputMessage.ID, messageToDispatch);

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (!keys.containsKey(keycode))
			return true;

		InputSource inputSource = keys.get(keycode);
		InputMessage messageToDispatch = InputMessage.obtain().setValue(0f)
				.setInputSource(inputSource);
		inputSource.getMessageDispatcher().dispatchMessage(null, null,
				InputMessage.ID, messageToDispatch);

		return true;
	}

	@Override
	public void dispose() {
		keys.clear();
	}
}
