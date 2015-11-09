package com.fabianachammer.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.fabianachammer.game.components.BodyComponent;
import com.fabianachammer.game.components.ShipComponent;
import com.fabianachammer.game.components.TransformComponent;

public class CameraMovementSystem extends EntitySystem {

	private Camera camera;
	private TransformComponent targetTransform;
	private BodyComponent targetBody;
	private ShipComponent targetShip;
	private float targetOffset;
	
	public CameraMovementSystem(Camera camera, Entity target, float targetOffset) {
		super();
		this.camera = camera;
		this.targetOffset = targetOffset;
		this.targetTransform = target.getComponent(TransformComponent.class);		
		this.targetBody = target.getComponent(BodyComponent.class);
		this.targetShip = target.getComponent(ShipComponent.class);
		
		if(targetTransform == null)
			throw new IllegalArgumentException("target must have a TransformComponent");
		
		if(targetBody == null)
			throw new IllegalArgumentException("target must have a BodyComponent");
		
		if(targetShip == null)
			throw new IllegalArgumentException("target must have a ShipComponent");
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		Vector3 targetPosition = new Vector3(targetTransform.getPosition(), 0);
		Vector3 lerpTarget = targetPosition.cpy().add(
				new Vector3(targetBody.getVelocity(), 0).nor().scl(targetOffset));

		float alpha = targetBody.getVelocity().len() / targetShip.getMaxSpeed();
		camera.position.set(targetPosition.lerp(lerpTarget, alpha));
		camera.update();
	}
}
