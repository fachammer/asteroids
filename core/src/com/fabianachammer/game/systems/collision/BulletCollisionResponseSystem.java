package com.fabianachammer.game.systems.collision;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.fabianachammer.game.components.BodyComponent;
import com.fabianachammer.game.components.CollisionFlags;
import com.fabianachammer.game.components.TransformComponent;
import com.fabianachammer.game.messages.CollisionMessage;
import com.fabianachammer.game.systems.ParticleSystem;

public class BulletCollisionResponseSystem extends CollisionResponseSystem implements EntityListener, Telegraph {

	private ParticleSystem particleEffectSystem;
	private ParticleEffect bulletCollisionEffect;
	private ComponentMapper<TransformComponent> transformMapper;
	private ComponentMapper<BodyComponent> bodyMapper;
	
	public BulletCollisionResponseSystem(ParticleSystem particleEffectSystem, TextureAtlas textureAtlas){
		this.particleEffectSystem = particleEffectSystem;
		transformMapper = ComponentMapper.getFor(TransformComponent.class);
		bodyMapper = ComponentMapper.getFor(BodyComponent.class);
		bulletCollisionEffect = new ParticleEffect();
		bulletCollisionEffect.load(Gdx.files.internal("sparks.p"), textureAtlas);
		bulletCollisionEffect.setEmittersCleanUpBlendFunction(false);
	}

	@Override
	protected boolean handleCollision(CollisionMessage collision) {
		engine.removeEntity(collision.self);
		particleEffectSystem.addParticleEffect(bulletCollisionEffect, 
				transformMapper.get(collision.self).getPosition(),
				bodyMapper.get(collision.self).getVelocity().angleRad() + MathUtils.PI);
		return true;
	}

	@Override
	protected boolean isPrimaryCollisionSubject(Entity entity) {
		return COLLIDER.get(entity).getFlag() == CollisionFlags.BULLET;
	}

	@Override
	protected boolean isSecondaryCollisionSubject(Entity entity) {
		return true;
	}
}
