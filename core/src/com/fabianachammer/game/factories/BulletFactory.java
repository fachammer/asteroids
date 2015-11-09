package com.fabianachammer.game.factories;

import java.util.EnumSet;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.fabianachammer.game.components.AutoDestructionComponent;
import com.fabianachammer.game.components.BodyComponent;
import com.fabianachammer.game.components.CircleColliderComponent;
import com.fabianachammer.game.components.CollisionFlags;
import com.fabianachammer.game.components.Player;
import com.fabianachammer.game.components.PlayerPossessedComponent;
import com.fabianachammer.game.components.ShapeComponent;
import com.fabianachammer.game.components.TransformComponent;

public class BulletFactory {

	private static final float BULLET_COLLISION_RADIUS = 0.2f;
	private static final Vector2[] BULLET_VERTICES = { new Vector2(-0.2f, 0f),
			new Vector2(0f, -0.2f), new Vector2(0.2f, 0f),
			new Vector2(0f, 0.2f) };

	public static Entity createBullet(Vector2 position, Vector2 velocity,
			float lifeTime, Player player) {
		Entity bullet = new Entity();

		bullet.add(new TransformComponent()
			.setPosition(position));
		
		bullet.add(new BodyComponent()
			.setVelocity(velocity)
			.setDrag(0));
		
		bullet.add(new ShapeComponent()
			.setColor(Color.YELLOW)
			.setVertices(BULLET_VERTICES));
		
		bullet.add(new AutoDestructionComponent()
			.setLifeTime(lifeTime)
			.setOutOfBoundsDestruction(true));
		
		bullet.add(new CircleColliderComponent()
			.setRadius(BULLET_COLLISION_RADIUS)
			.setFlag(CollisionFlags.BULLET)
			.setMask(EnumSet.of(CollisionFlags.ASTEROID)));
		
		bullet.add(new PlayerPossessedComponent(player));

		return bullet;
	}
}
