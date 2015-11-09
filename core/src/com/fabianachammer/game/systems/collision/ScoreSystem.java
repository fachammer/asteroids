package com.fabianachammer.game.systems.collision;

import java.util.EnumSet;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.fabianachammer.game.components.CollisionFlags;
import com.fabianachammer.game.components.Player;
import com.fabianachammer.game.components.PlayerPossessedComponent;
import com.fabianachammer.game.components.TransformComponent;
import com.fabianachammer.game.messages.CollisionMessage;

public class ScoreSystem extends CollisionResponseSystem {

	private final ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
	private final ComponentMapper<PlayerPossessedComponent> playerPossessionMapper = ComponentMapper.getFor(PlayerPossessedComponent.class);
	
	private float scoreToScaleFactor;
	
	public ScoreSystem(float scoreToScaleFactor){
		this.scoreToScaleFactor = scoreToScaleFactor;
	}
	
	@Override
	protected boolean isSecondaryCollisionSubject(Entity entity) {
		return COLLIDER.get(entity).getFlag() == CollisionFlags.ASTEROID;
	}

	@Override
	protected boolean isPrimaryCollisionSubject(Entity entity) {
		return EnumSet.of(CollisionFlags.BULLET, CollisionFlags.ORB, CollisionFlags.SHIP).contains(COLLIDER.get(entity).getFlag());
	}

	@Override
	protected boolean handleCollision(CollisionMessage collision) {
		PlayerPossessedComponent playerPossesion = playerPossessionMapper.get(collision.self);
		Player shootingPlayer = playerPossesion.getPlayer();
		
		float asteroidScale = transformMapper.get(collision.other).getScale().x;
		float scoreIncrease = scoreToScaleFactor / asteroidScale;
		
		if(COLLIDER.get(collision.self).getFlag() == CollisionFlags.SHIP)
			scoreIncrease = -scoreIncrease;
		
		shootingPlayer.changeScore(MathUtils.ceil(scoreIncrease));
		
		return true;
	}
}
