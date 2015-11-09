package com.fabianachammer.game.components;

public enum CollisionFlags {
	
	SHIP,
	BULLET,
	ASTEROID, 
	SHIELD_PICKUP,
	ORB;
	
	private final int flag;
	
	private CollisionFlags(){
		flag = 1 << ordinal();
	}
	
	public int getFlag(){
		return flag;
	}
}
