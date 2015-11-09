package com.fabianachammer.game.systems.collision;

import java.util.EnumSet;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.RandomXS128;
import com.fabianachammer.game.components.CollisionFlags;
import com.fabianachammer.game.components.TransformComponent;
import com.fabianachammer.game.factories.AsteroidGenerator;
import com.fabianachammer.game.factories.ShieldPickupFactory;
import com.fabianachammer.game.messages.CollisionMessage;
import com.fabianachammer.game.systems.ParticleSystem;

public class AsteroidCollisionResponseSystem extends CollisionResponseSystem {

	private static final float DROP_PROBABILITY = 0.1f;
	private static final ComponentMapper<TransformComponent> TRANSFORM = ComponentMapper
			.getFor(TransformComponent.class);
	private static final RandomXS128 RANDOM = new RandomXS128();

	private int asteroidDivisions;
	private ParticleSystem particleSystem;
	private ParticleEffect asteroidDestructionEffect;
	private Sound asteroidDestructionSound;

	public AsteroidCollisionResponseSystem(int asteroidDivisions,
			ParticleSystem particleSystem, TextureAtlas textureAtlas) {
		this.asteroidDivisions = asteroidDivisions;
		this.particleSystem = particleSystem;
		asteroidDestructionEffect = new ParticleEffect();
		asteroidDestructionEffect.load(Gdx.files.internal("asteroid-sparks.p"),
				textureAtlas);
		asteroidDestructionEffect.setEmittersCleanUpBlendFunction(false);
		asteroidDestructionSound = Gdx.audio.newSound(Gdx.files
				.internal("asteroid-destruction.wav"));
	}

	@Override
	protected boolean isPrimaryCollisionSubject(Entity entity) {
		return COLLIDER.get(entity).getFlag() == CollisionFlags.ASTEROID
				&& TRANSFORM.has(entity);
	}

	@Override
	protected boolean isSecondaryCollisionSubject(Entity entity) {
		return EnumSet.of(CollisionFlags.BULLET, CollisionFlags.SHIP,
				CollisionFlags.ORB).contains(
				COLLIDER.get(entity).getFlag());
	}

	private float nextFloatInRange(float min, float max) {
		return RANDOM.nextFloat() * (max - min) + min;
	}

	@Override
	protected boolean handleCollision(CollisionMessage collision) {
		engine.removeEntity(collision.self);

		TransformComponent asteroidTransform = TRANSFORM.get(collision.self);

		particleSystem.addParticleEffect(asteroidDestructionEffect,
				asteroidTransform.getPosition(), 0f);
		asteroidDestructionSound.play(nextFloatInRange(0.15f, 0.25f),
				nextFloatInRange(0.78f, 0.82f), 0);

		RandomXS128 random = new RandomXS128();
		if (TRANSFORM.get(collision.self).getScale().x <= 1) {

			if (COLLIDER.get(collision.other).getFlag() != CollisionFlags.BULLET
					|| random.nextFloat() > DROP_PROBABILITY)
				return true;

			Entity shieldPickupEntity = ShieldPickupFactory.createShieldPickup(
					TRANSFORM.get(collision.self).getPosition().cpy(), random);
			engine.addEntity(shieldPickupEntity);

			return true;
		}

		for (int i = 0; i < asteroidDivisions; i++) {
			Entity asteroidPart = AsteroidGenerator.generateAsteroid(random, 0,
					10, 2);
			TransformComponent asteroidPartTransform = TRANSFORM
					.get(asteroidPart);
			asteroidPartTransform.setPosition(asteroidTransform.getPosition()
					.cpy());
			asteroidPartTransform.setScale(asteroidTransform.getScale().cpy()
					.scl(0.5f));
			engine.addEntity(asteroidPart);
		}

		return true;
	}

}
