package com.fabianachammer.game.factories;

import java.util.EnumSet;

import com.badlogic.ashley.core.Entity;
import com.fabianachammer.game.components.AttractionComponent;
import com.fabianachammer.game.components.BodyComponent;
import com.fabianachammer.game.components.CircleColliderComponent;
import com.fabianachammer.game.components.CollisionFlags;
import com.fabianachammer.game.components.Player;
import com.fabianachammer.game.components.PlayerPossessedComponent;
import com.fabianachammer.game.components.ShapeComponent;
import com.fabianachammer.game.components.ShieldComponent;
import com.fabianachammer.game.components.ShipComponent;
import com.fabianachammer.game.components.ShooterComponent;
import com.fabianachammer.game.components.TransformComponent;

public class ShipFactory {

	public static Entity createShip(ShipData shipData, Player player) {
		Entity ship = new Entity();

		ship.add(new TransformComponent());
		ship.add(new BodyComponent()
			.setMass(shipData.mass)
			.setDrag(shipData.drag)
			.setAngleDrag(shipData.angleDrag));
		
		ship.add(new ShapeComponent()
			.setColor(shipData.color)
			.setVertices(shipData.vertices));
		
		ship.add(new ShipComponent()
			.setAcceleration(shipData.acceleration)
			.setAngleAcceleration(shipData.angleAcceleration)
			.setMaxSpeed(shipData.maxSpeed)
			.setMaxAngleSpeed(shipData.maxAngleSpeed));
		
		ship.add(new ShooterComponent()
			.setFireRate(shipData.fireRate)
			.setGunOffset(shipData.gunOffset)
			.setBulletSpeed(shipData.bulletSpeed)
			.setBulletLifeTime(shipData.bulletLifeTime));
		
		ship.add(new CircleColliderComponent()
			.setRadius(shipData.collisionRadius)
			.setFlag(CollisionFlags.SHIP)
			.setMask(EnumSet.of(CollisionFlags.ASTEROID, CollisionFlags.SHIELD_PICKUP)));
		
		ship.add(new ShieldComponent()
			.setMaxShield(5)
			.setCurrentShield(5)
			.setRegenerationCooldown(2)
			.setRegenerationRate(1));
		
		ship.add(new AttractionComponent()
			.setAttractionFlag(CollisionFlags.SHIP)
			.setAttractionMask(EnumSet.of(CollisionFlags.SHIELD_PICKUP))
			.setAttractionForce(15000)
			.setAttractionThreshold(12));
		
		ship.add(new PlayerPossessedComponent(player));

		return ship;
	}
}
