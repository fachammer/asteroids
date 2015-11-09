package com.fabianachammer.game.messages;

import com.badlogic.ashley.core.Entity;

public class CollisionMessage {

	public static final int ID = 100;
	
	public Entity self;
	public Entity other;
	
	public CollisionMessage(Entity self, Entity other){
		this.self = self;
		this.other = other;
	}
}
