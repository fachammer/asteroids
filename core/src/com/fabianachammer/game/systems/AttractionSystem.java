package com.fabianachammer.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.fabianachammer.game.components.AttractionComponent;
import com.fabianachammer.game.components.BodyComponent;
import com.fabianachammer.game.components.TransformComponent;

public class AttractionSystem extends IteratingSystem {

	private static final ComponentMapper<AttractionComponent> ATTRACTION = ComponentMapper
			.getFor(AttractionComponent.class);
	private static final ComponentMapper<BodyComponent> BODY = ComponentMapper
			.getFor(BodyComponent.class);
	private static final ComponentMapper<TransformComponent> TRANSFORM = ComponentMapper
			.getFor(TransformComponent.class);

	public AttractionSystem() {
		super(Family.all(AttractionComponent.class, BodyComponent.class,
				TransformComponent.class).get());
	}

	@Override
	protected void processEntity(Entity selfEntity, float deltaTime) {
		AttractionComponent selfAttraction = ATTRACTION.get(selfEntity);
		TransformComponent selfTransform = TRANSFORM.get(selfEntity);
		for (Entity otherEntity : getEntities()) {
			if (otherEntity == selfEntity)
				continue;

			AttractionComponent otherAttraction = ATTRACTION.get(otherEntity);
			BodyComponent otherBody = BODY.get(otherEntity);
			TransformComponent otherTransform = TRANSFORM.get(otherEntity);

			if (!selfAttraction.getAttractionMask().contains(
					otherAttraction.getAttractionFlag()))
				continue;

			Vector2 deltaPosition = selfTransform.getPosition().cpy()
					.sub(otherTransform.getPosition());

			if (deltaPosition.len2() > selfAttraction.getAttractionThreshold()
					* selfAttraction.getAttractionThreshold())
				continue;

			Vector2 attractionForce = deltaPosition
					.cpy()
					.nor()
					.scl(selfAttraction.getAttractionForce()
							/ deltaPosition.len2());
			PhysicsSystem.applyForce(otherBody, attractionForce);
		}
	}
}
