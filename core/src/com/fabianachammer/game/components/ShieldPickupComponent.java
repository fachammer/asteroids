package com.fabianachammer.game.components;

import com.badlogic.ashley.core.Component;

public class ShieldPickupComponent extends Component {

	private float shieldIncrease;

	public float getShieldIncrease() {
		return shieldIncrease;
	}

	public ShieldPickupComponent setShieldIncrease(float shieldIncrease) {
		this.shieldIncrease = shieldIncrease;
		return this;
	}
}
