package com.fabianachammer.engine.input;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.ashley.core.EntitySystem;

public class InputSystem extends EntitySystem {

	private Set<InputSource> inputSources = new HashSet<InputSource>();
	
	public void addInputSource(InputSource inputSource){
		inputSources.add(inputSource);
	}
	
	public void removeInputSource(InputSource inputSource){
		inputSources.remove(inputSource);
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		inputSources.forEach(input -> input.update(deltaTime));
	}
}
