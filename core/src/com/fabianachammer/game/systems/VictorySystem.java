package com.fabianachammer.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.fabianachammer.game.components.CircleColliderComponent;
import com.fabianachammer.game.components.CollisionFlags;

public class VictorySystem extends EntitySystem implements EntityListener {

	public static final int VICTORY_MESSAGE_ID = 600;
	private static final ComponentMapper<CircleColliderComponent> COLLIDER = ComponentMapper.getFor(CircleColliderComponent.class);
	
	private int asteroidsCount;
	
	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		engine.addEntityListener(this);
	}
	
	@Override
	public void removedFromEngine(Engine engine) {
		super.removedFromEngine(engine);
		engine.removeEntityListener(this);
	}
	
	@Override
	public void entityAdded(Entity entity) {
		if(isAsteroid(entity))
			asteroidsCount++;
	}

	@Override
	public void entityRemoved(Entity entity) {
		if(isAsteroid(entity))
			asteroidsCount--;
		
		if(asteroidsCount == 0)
			MessageManager.getInstance().dispatchMessage(null, VICTORY_MESSAGE_ID);
	}

	private boolean isAsteroid(Entity entity){
		return COLLIDER.has(entity) && COLLIDER.get(entity).getFlag() == CollisionFlags.ASTEROID;
	}
}
