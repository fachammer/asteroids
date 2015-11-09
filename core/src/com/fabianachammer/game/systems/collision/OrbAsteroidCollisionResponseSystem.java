package com.fabianachammer.game.systems.collision;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.fabianachammer.game.components.CollisionFlags;
import com.fabianachammer.game.components.TransformComponent;
import com.fabianachammer.game.messages.CollisionMessage;
import com.fabianachammer.game.systems.ParticleSystem;

public class OrbAsteroidCollisionResponseSystem extends CollisionResponseSystem {

	private ParticleSystem particleSystem;
	private ParticleEffect orbDestructionEffect;
	private static final ComponentMapper<TransformComponent> TRANSFORM = ComponentMapper.getFor(TransformComponent.class);
	
	public OrbAsteroidCollisionResponseSystem(ParticleSystem particleSystem, TextureAtlas textureAtlas) {
		this.particleSystem = particleSystem;
		orbDestructionEffect = new ParticleEffect();
		orbDestructionEffect.load(Gdx.files.internal("asteroid-sparks.p"), textureAtlas);
		orbDestructionEffect.setEmittersCleanUpBlendFunction(false);
	}
	
	@Override
	protected boolean isSecondaryCollisionSubject(Entity entity) {
		return COLLIDER.get(entity).getFlag() == CollisionFlags.ASTEROID;
	}

	@Override
	protected boolean isPrimaryCollisionSubject(Entity entity) {
		return COLLIDER.get(entity).getFlag() == CollisionFlags.ORB;
	}

	@Override
	protected boolean handleCollision(CollisionMessage collision) {
		Vector2 scale = TRANSFORM.get(collision.self).getScale();
		
		if(scale.x < 0.5f)
			engine.removeEntity(collision.self);
		
		scale.scl(0.8f);
		
		particleSystem.addParticleEffect(orbDestructionEffect, TRANSFORM.get(collision.self).getPosition(), 0f);
		return true;
	}

}
