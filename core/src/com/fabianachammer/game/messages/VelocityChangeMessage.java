package com.fabianachammer.game.messages;

import com.badlogic.gdx.math.Vector2;
import com.fabianachammer.game.components.BodyComponent;

public class VelocityChangeMessage {

	public static final int ID = 200;

	public BodyComponent body;
	public Vector2 oldVelocity;
	public Vector2 newVelocity;
	
	public VelocityChangeMessage(BodyComponent body, Vector2 oldVelocity, Vector2 newVelocity){
		this.body = body;
		this.oldVelocity = oldVelocity;
		this.newVelocity = newVelocity;
	}
}
