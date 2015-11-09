package com.fabianachammer.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.fabianachammer.game.components.BodyComponent;
import com.fabianachammer.game.components.TransformComponent;
import com.fabianachammer.game.messages.VelocityChangeMessage;

public class PhysicsSystem extends IteratingSystem {

	public static float MOVEMENT_THRESHOLD = 1f;
	public static float ANGLE_MOVEMENT_THRESHOLD = 2f;

	private ComponentMapper<TransformComponent> tm;
	private ComponentMapper<BodyComponent> bm;

	public PhysicsSystem() {
		super(Family.all(TransformComponent.class, BodyComponent.class).get());
		tm = ComponentMapper.getFor(TransformComponent.class);
		bm = ComponentMapper.getFor(BodyComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		TransformComponent transform = tm.get(entity);
		BodyComponent body = bm.get(entity);

		updatePhysics(transform, body, deltaTime);
	}

	private static void updatePhysics(TransformComponent transform, BodyComponent body,
			float deltaTime) {
		avoidJittering(body);
		applyDrag(body);
		accelerate(body, deltaTime);
		move(transform, body, deltaTime);
		resetAcceleration(body);
	}

	private static void resetAcceleration(BodyComponent body) {
		body.getAcceleration().setZero();
		body.setAngleAcceleration(0);
	}

	private static void move(TransformComponent transform, BodyComponent body,
			float deltaTime) {
		transform.getPosition().mulAdd(body.getVelocity(), deltaTime);
		transform.setAngle(transform.getAngle() + body.getAngleVelocity() * deltaTime);
	}

	private static void accelerate(BodyComponent body, float deltaTime) {
		Vector2 oldVelocity = body.getVelocity().cpy();
		body.getVelocity().mulAdd(body.getAcceleration(), deltaTime);
		body.setAngleVelocity(body.getAngleVelocity() + body.getAngleAcceleration() * deltaTime);

		body.getMessageDispatcher().dispatchMessage(null, VelocityChangeMessage.ID, new VelocityChangeMessage(body, oldVelocity, body.getVelocity().cpy()));
	}

	private static void applyDrag(BodyComponent body) {
		Vector2 drag = body.getVelocity().cpy().scl(-body.getDrag());
		applyForce(body, drag);

		float angleDrag = body.getAngleVelocity() * (-body.getAngleDrag());
		applyTorque(body, angleDrag);
	}

	private static void avoidJittering(BodyComponent body) {
		if (body.getVelocity().isZero(MOVEMENT_THRESHOLD))
			body.getVelocity().setZero();
		if (Math.abs(body.getAngleVelocity()) < ANGLE_MOVEMENT_THRESHOLD)
			body.setAngleVelocity(0);
	}

	public static void applyForce(BodyComponent body, Vector2 force) {
		body.getAcceleration().add(force.cpy().scl(1f / body.getMass()));
	}

	public static void applyTorque(BodyComponent body, float torque) {
		body.setAngleAcceleration(body.getAngleAcceleration() + torque / body.getMass());
	}
}
