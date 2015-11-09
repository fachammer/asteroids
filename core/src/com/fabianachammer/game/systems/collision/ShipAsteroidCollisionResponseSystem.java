package com.fabianachammer.game.systems.collision;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.fabianachammer.game.components.CollisionFlags;
import com.fabianachammer.game.components.ShieldComponent;
import com.fabianachammer.game.components.ShipComponent;
import com.fabianachammer.game.messages.CollisionMessage;
import com.fabianachammer.game.systems.ShieldSystem;

public class ShipAsteroidCollisionResponseSystem extends CollisionResponseSystem {

	private ComponentMapper<ShipComponent> shipMapper;
	private ComponentMapper<ShieldComponent> shieldMapper;
	
	public ShipAsteroidCollisionResponseSystem() {
		shipMapper = ComponentMapper.getFor(ShipComponent.class);
		shieldMapper = ComponentMapper.getFor(ShieldComponent.class);
	}

	@Override
	protected boolean isPrimaryCollisionSubject(Entity entity) {
		return shipMapper.has(entity) && shieldMapper.has(entity);
	}
	
	@Override
	protected boolean isSecondaryCollisionSubject(Entity entity) {
		return COLLIDER.get(entity).getFlag() == CollisionFlags.ASTEROID;
	}

	@Override
	protected boolean handleCollision(CollisionMessage collision) {
		ShieldComponent shield = shieldMapper.get(collision.self);
		ShieldSystem.changeShield(shield, -1);
		return true;
	}
}
