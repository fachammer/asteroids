package com.fabianachammer.game.factories;

import java.util.EnumSet;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.fabianachammer.game.components.BodyComponent;
import com.fabianachammer.game.components.CircleColliderComponent;
import com.fabianachammer.game.components.CollisionFlags;
import com.fabianachammer.game.components.FlockingComponent;
import com.fabianachammer.game.components.Player;
import com.fabianachammer.game.components.PlayerPossessedComponent;
import com.fabianachammer.game.components.ShapeComponent;
import com.fabianachammer.game.components.TransformComponent;

public class OrbFactory {

	public static Entity createOrb(Vector2 position, Entity target, Player player){
		Entity orb = new Entity();
		orb.add(new TransformComponent()
					.setPosition(position.cpy())
					.setUniformScale(1.5f));
		orb.add(new BodyComponent()
					.setDrag(2f));
		orb.add(new FlockingComponent()
					.setCohesion(1)
					.setRepulsion(2.5f)
					.setAlignment(1)
					.setTargetAttraction(1.5f)
					.setTangentialAttraction(1.5f)
					.setRepulsionThreshold(7)
					.setCohesionThreshold(7)
					.setAlignmentThreshold(7)
					.setTarget(target));
		
		ShapeComponent shape = new ShapeComponent()
								   .setVertices(ShieldPickupFactory.SHIELD_PICKUP_VERTICES)
								   .setColor(Color.MAGENTA);
		orb.add(shape);
		orb.add(CircleColliderComponent.createFromShape(shape)
									    .setFlag(CollisionFlags.ORB)
									    .setMask(EnumSet.of(CollisionFlags.ASTEROID)));
		
		orb.add(new PlayerPossessedComponent(player));
		
		return orb;
	}
}
