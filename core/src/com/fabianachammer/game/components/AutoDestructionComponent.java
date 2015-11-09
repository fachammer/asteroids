package com.fabianachammer.game.components;

import com.badlogic.ashley.core.Component;

public class AutoDestructionComponent extends Component {

	private float lifeTime = 0f;
	private boolean outOfBoundsDestruction = false;

	public float getLifeTime() {
		return lifeTime;
	}

	public AutoDestructionComponent setLifeTime(float lifeTime) {
		this.lifeTime = lifeTime;
		return this;
	}
	
	public AutoDestructionComponent decreaseLifeTime(float decreaseAmount){
		this.lifeTime -= decreaseAmount;
		return this;
	}
	
	public boolean isOutOfBoundsDestruction(){
		return outOfBoundsDestruction;
	}
	
	public AutoDestructionComponent setOutOfBoundsDestruction(boolean outOfBoundsDestruction){
		this.outOfBoundsDestruction = outOfBoundsDestruction;
		return this;
	}
}