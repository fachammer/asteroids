package com.fabianachammer.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.fabianachammer.game.components.AutoDestructionComponent;
import com.fabianachammer.game.components.TransformComponent;

public class AutoDestructionSystem extends IteratingSystem {

	private static final ComponentMapper<AutoDestructionComponent> AUTO_DESTRUCTION = ComponentMapper
			.getFor(AutoDestructionComponent.class);
	private static final ComponentMapper<TransformComponent> TRANSFORM = ComponentMapper
			.getFor(TransformComponent.class);

	private Engine engine;
	private float width;
	private float height;

	public AutoDestructionSystem(float width, float height) {
		super(Family.all(AutoDestructionComponent.class).get());
		this.width = width;
		this.height = height;
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		this.engine = engine;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		if (shouldDestroyEntity(entity))
			engine.removeEntity(entity);
		else
			AUTO_DESTRUCTION.get(entity).decreaseLifeTime(deltaTime);
	}

	private boolean shouldDestroyEntity(Entity entity) {
		AutoDestructionComponent autoDestruct = AUTO_DESTRUCTION.get(entity);
		return autoDestruct.getLifeTime() <= 0f || autoDestruct.isOutOfBoundsDestruction() && isOutOfBounds(entity);
	}

	private boolean isOutOfBounds(Entity entity) {
		if(!TRANSFORM.has(entity))
			return false;
		
		Vector2 position = TRANSFORM.get(entity).getPosition();
		Vector2 scale = TRANSFORM.get(entity).getScale();
		return position.x > width - scale.x / 2
				|| position.y > height - scale.y / 2
				|| position.x < scale.x / 2 || position.y < scale.y / 2;
	}
}
