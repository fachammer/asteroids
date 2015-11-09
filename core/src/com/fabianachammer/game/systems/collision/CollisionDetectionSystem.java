package com.fabianachammer.game.systems.collision;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.math.Vector2;
import com.fabianachammer.game.components.CircleColliderComponent;
import com.fabianachammer.game.components.TransformComponent;
import com.fabianachammer.game.messages.CollisionMessage;

public class CollisionDetectionSystem extends EntitySystem {

	private ImmutableArray<Entity> entities;
	private static final ComponentMapper<TransformComponent> TRANSFORM = ComponentMapper
			.getFor(TransformComponent.class);
	private static final ComponentMapper<CircleColliderComponent> COLLIDER = ComponentMapper
			.getFor(CircleColliderComponent.class);
	private final MessageDispatcher messageDispatcher = MessageManager
			.getInstance();;

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		entities = engine.getEntitiesFor(Family.all(TransformComponent.class,
				CircleColliderComponent.class).get());
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		for (int i = 0; i < entities.size(); i++) {
			Entity entityA = entities.get(i);
			for (int j = i + 1; j < entities.size(); j++) {
				Entity entityB = entities.get(j);
				if (areEntitiesColliding(entityA, entityB)) {
					COLLIDER.get(entityA)
							.getMessageDispatcher()
							.dispatchMessage(null, CollisionMessage.ID,
									new CollisionMessage(entityA, entityB));
					COLLIDER.get(entityB)
							.getMessageDispatcher()
							.dispatchMessage(null, CollisionMessage.ID,
									new CollisionMessage(entityB, entityA));
					messageDispatcher.dispatchMessage(null,
							CollisionMessage.ID, new CollisionMessage(entityA,
									entityB));
				}
			}
		}

		messageDispatcher.update(deltaTime);
	}

	private boolean canCollideAccordingToLayers(Entity entityA, Entity entityB) {
		CircleColliderComponent circleA = COLLIDER.get(entityA);
		CircleColliderComponent circleB = COLLIDER.get(entityB);
		return circleB.getMask().contains(circleA.getFlag())
				&& circleA.getMask().contains(circleB.getFlag());
	}

	private boolean hasTransformAndCollider(Entity entity) {
		return TRANSFORM.has(entity) && COLLIDER.has(entity);
	}

	private float square(float number) {
		return number * number;
	}

	private boolean areEntitiesIntersecting(Entity entityA, Entity entityB) {
		TransformComponent transformA = TRANSFORM.get(entityA);
		CircleColliderComponent circleA = COLLIDER.get(entityA);
		TransformComponent transformB = TRANSFORM.get(entityB);
		CircleColliderComponent circleB = COLLIDER.get(entityB);

		float radiusA = circleA.getRadius() * transformA.getScale().x;
		float radiusB = circleB.getRadius() * transformB.getScale().x;

		Vector2 deltaLocation = transformA.getPosition().cpy()
				.sub(transformB.getPosition());
		return deltaLocation.len2() <= square(radiusA + radiusB);
	}

	public boolean areEntitiesColliding(Entity entityA, Entity entityB) {
		if (!hasTransformAndCollider(entityA)
				|| !hasTransformAndCollider(entityB)) {
			System.err.println("collision entities must both have a "
					+ "TransformComponent and a CircleColliderComponent");
			return false;
		}

		return canCollideAccordingToLayers(entityA, entityB)
				&& areEntitiesIntersecting(entityA, entityB);
	}
}
