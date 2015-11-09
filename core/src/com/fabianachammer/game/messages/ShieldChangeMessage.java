package com.fabianachammer.game.messages;

import com.fabianachammer.game.components.ShieldComponent;

public class ShieldChangeMessage {

	public static final int ID = 300;
	
	public ShieldComponent shield;
	public float oldShield;
	public float newShield;
	
	public ShieldChangeMessage(ShieldComponent shield, float oldShield,
			float newShield) {
		this.shield = shield;
		this.oldShield = oldShield;
		this.newShield = newShield;
	}
}
