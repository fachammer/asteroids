package com.fabianachammer.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.fabianachammer.game.components.TransformComponent;

public class PipingSystem extends IteratingSystem {

	private static final ComponentMapper<TransformComponent> TRANSFORM= ComponentMapper.getFor(TransformComponent.class);
	
	private float width;
	private float height;
	
	public PipingSystem(float width, float height) {
		super(Family.all(TransformComponent.class).get());
		this.width = width;
		this.height = height;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		TransformComponent transform = TRANSFORM.get(entity);
		Vector2 position = transform.getPosition();
		Vector2 scale = transform.getScale();
		
		position.x = cycle(position.x, -scale.x, width + scale.x);
		position.y = cycle(position.y, -scale.y, height + scale.y);
	}

	private static float cycle(float value, float min, float max) {
		while (value >= max || value < min)
			value -= Math.signum(value) * (max - min);

		return value;
	}
}
