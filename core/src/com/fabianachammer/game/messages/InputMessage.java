package com.fabianachammer.game.messages;

import com.badlogic.gdx.utils.Pool;
import com.fabianachammer.engine.input.InputSource;

public class InputMessage {

	public static final int ID = 400;
	
	private float value;
	private InputSource source;
	
	private static final Pool<InputMessage> pool = new Pool<InputMessage>(){

		@Override
		protected InputMessage newObject() {
			return new InputMessage();
		}
		
	};
	
	public static InputMessage obtain(){
		return pool.obtain();
	}
	
	public static void free(InputMessage message){
		pool.free(message);
	}
	
	public float getValue(){
		return value;
	}
	
	public InputMessage setValue(float value){
		this.value = value;
		return this;
	}
	
	public InputSource getInputSource(){
		return source;
	}
	
	public InputMessage setInputSource(InputSource source) {
		this.source = source;
		return this;
	}
}
