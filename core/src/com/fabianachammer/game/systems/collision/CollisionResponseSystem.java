package com.fabianachammer.game.systems.collision;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.fabianachammer.game.components.CircleColliderComponent;
import com.fabianachammer.game.messages.CollisionMessage;

public abstract class CollisionResponseSystem extends EntitySystem implements
		EntityListener, Telegraph {

	protected Engine engine;
	protected static final ComponentMapper<CircleColliderComponent> COLLIDER = ComponentMapper
			.getFor(CircleColliderComponent.class);;

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		this.engine = engine;
		for (Entity entity : engine.getEntities())
			entityAdded(entity);
		engine.addEntityListener(this);
	}

	@Override
	public void removedFromEngine(Engine engine) {
		super.removedFromEngine(engine);
		engine.removeEntityListener(this);
		this.engine = null;
	}

	@Override
	public void entityAdded(Entity entity) {
		if (COLLIDER.has(entity) && isPrimaryCollisionSubject(entity)) {
			MessageDispatcher colliderMessageDispatcher = COLLIDER.get(entity)
					.getMessageDispatcher();
			colliderMessageDispatcher.addListener(this, CollisionMessage.ID);
		}
	}

	@Override
	public void entityRemoved(Entity entity) {
		if (COLLIDER.has(entity) && isPrimaryCollisionSubject(entity)) {
			MessageDispatcher colliderMessageDispatcher = COLLIDER.get(entity)
					.getMessageDispatcher();
			colliderMessageDispatcher.removeListener(this, CollisionMessage.ID);
		}
	}

	protected abstract boolean isSecondaryCollisionSubject(Entity entity);

	protected abstract boolean isPrimaryCollisionSubject(Entity entity);

	@Override
	public boolean handleMessage(Telegram msg) {
		CollisionMessage collision = (CollisionMessage) msg.extraInfo;
		return isSecondaryCollisionSubject(collision.other)
				&& handleCollision(collision);
	}

	protected abstract boolean handleCollision(CollisionMessage collision);
}