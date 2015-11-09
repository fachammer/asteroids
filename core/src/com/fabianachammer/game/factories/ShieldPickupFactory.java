package com.fabianachammer.game.factories;

import java.util.EnumSet;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.fabianachammer.game.components.AttractionComponent;
import com.fabianachammer.game.components.AutoDestructionComponent;
import com.fabianachammer.game.components.BodyComponent;
import com.fabianachammer.game.components.CircleColliderComponent;
import com.fabianachammer.game.components.CollisionFlags;
import com.fabianachammer.game.components.ShapeComponent;
import com.fabianachammer.game.components.ShieldPickupComponent;
import com.fabianachammer.game.components.TransformComponent;

public class ShieldPickupFactory {

	public static Vector2[] SHIELD_PICKUP_VERTICES = {
		new Vector2(0.5f, 0), new Vector2(0, 0.5f),
		new Vector2(-0.5f, 0), new Vector2(0, -0.5f)
	};
	
	public static Entity createShieldPickup(Vector2 position, RandomXS128 random) {
		Entity shieldPickupEntity = new Entity();
		shieldPickupEntity.add(new AutoDestructionComponent()
			.setLifeTime(10));
		
		shieldPickupEntity.add(new BodyComponent()
			.setMass(1)
			.setDrag(0.5f)
			.setVelocity(new Vector2(
					random.nextFloat() * 2 * (float) 5 - 5,
					random.nextFloat() * 2 * 5 - 5)));
		
		ShapeComponent shape = new ShapeComponent()
			.setColor(Color.MAGENTA)
			.setVertices(SHIELD_PICKUP_VERTICES);
		shieldPickupEntity.add(shape);
		
		shieldPickupEntity.add(new TransformComponent()
			.setPosition(position.cpy())
			.setUniformScale(1.5f));
		
		shieldPickupEntity.add(new ShieldPickupComponent()
			.setShieldIncrease(1));
		
		shieldPickupEntity.add(CircleColliderComponent.createFromShape(shape)
			.setFlag(CollisionFlags.SHIELD_PICKUP)
			.setMask(EnumSet.of(CollisionFlags.SHIP)));
		
		shieldPickupEntity.add(new AttractionComponent()
			.setAttractionFlag(CollisionFlags.SHIELD_PICKUP)
			.setAttractionMask(EnumSet.noneOf(CollisionFlags.class))
			.setAttractionForce(0));
					
		
		return shieldPickupEntity;
	}


}
