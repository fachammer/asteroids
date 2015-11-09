package com.fabianachammer.game.systems;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ParticleSystem extends EntitySystem {

	private Map<ParticleEffect, ParticleEffectPool> effectPools;
	private Set<PooledEffect> effectsToRender;
	private Set<PooledEffect> effectsToRemove;
	private SpriteBatch spriteBatch;
	private Camera camera;
	
	public ParticleSystem(Camera camera) {
		super();
		this.camera = camera;
		effectPools = new HashMap<ParticleEffect, ParticleEffectPool>();
		effectsToRender = new HashSet<PooledEffect>();
		effectsToRemove = new HashSet<PooledEffect>();
		spriteBatch = new SpriteBatch();
	}

	public void addParticleEffect(ParticleEffect particleEffect, Vector2 position, float rotation){
		effectPools.putIfAbsent(particleEffect, new ParticleEffectPool(particleEffect, 1, 10));
		ParticleEffectPool pool = effectPools.get(particleEffect);
		PooledEffect actualEffect = pool.obtain();
		actualEffect.setPosition(position.x, position.y);
		actualEffect.setDuration(100);
		setRotation(actualEffect, rotation);
		setScale(actualEffect, 0.3f);
		effectsToRender.add(actualEffect);
	}
	
	private static void setRotation(ParticleEffect particleEffect, float rotation){
		Array<ParticleEmitter> emitters = particleEffect.getEmitters();        
        for (int i = 0; i < emitters.size; i++) {  
           ScaledNumericValue val = emitters.get(i).getAngle();
           float amplitude = (val.getHighMax() - val.getHighMin()) / 2f;
           float h1 = rotation * MathUtils.radiansToDegrees + amplitude;                                            
           float h2 =  rotation * MathUtils.radiansToDegrees - amplitude;                                            
           val.setHigh(h1, h2);                                           
           val.setLow( rotation * MathUtils.degreesToRadians);       
        }
	}
	
	private static void setScale(ParticleEffect particleEffect, float scale){
		Array<ParticleEmitter> emitters = particleEffect.getEmitters();        
        for (int i = 0; i < emitters.size; i++) {  
           ScaledNumericValue val = emitters.get(i).getScale();
           float amplitude = (val.getHighMax() - val.getHighMin()) / 2f;
           float h1 = scale + amplitude;                                            
           float h2 =  scale - amplitude;                                            
           val.setHigh(h1, h2);                                           
           val.setLow(scale);       
        }
	}
	
	public void update(float deltaTime) {
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		effectsToRender.forEach(effect -> {
		    effect.draw(spriteBatch, deltaTime);
		    if (effect.isComplete()) {
		        effect.free();
		        effectsToRemove.add(effect);
		    }
		});
		
		effectsToRender.removeAll(effectsToRemove);
		effectsToRemove.clear();
		spriteBatch.end();
	}
	
	public void removedFromEngine(Engine engine) {
		effectsToRender.forEach(effect -> effect.free());
		effectsToRender.clear();
	}
}
