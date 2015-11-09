package com.fabianachammer.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.fabianachammer.engine.input.InputSource;
import com.fabianachammer.game.VirtualGamepad;
import com.fabianachammer.game.components.BodyComponent;
import com.fabianachammer.game.components.ShipComponent;
import com.fabianachammer.game.components.TransformComponent;
import com.fabianachammer.game.messages.InputMessage;

public class ShipMovementSystem extends IteratingSystem {

	private ComponentMapper<BodyComponent> bm;
	private ComponentMapper<TransformComponent> tm;
	private ComponentMapper<ShipComponent> sm;
	
	private InputSource rotationInput;
	private InputSource thrustInput;
	private ParticleSystem particleSystem;
	private ParticleEffect thrustParticleEffect;

	public ShipMovementSystem(VirtualGamepad virtualGamepad, ParticleSystem particleSystem, TextureAtlas textureAtlas) {
		super(Family.all(TransformComponent.class, BodyComponent.class,
				ShipComponent.class).get());
		this.rotationInput = virtualGamepad.getRotationInput();
		this.thrustInput = virtualGamepad.getThrustInput();
		this.particleSystem = particleSystem;
		bm = ComponentMapper.getFor(BodyComponent.class);
		tm = ComponentMapper.getFor(TransformComponent.class);
		sm = ComponentMapper.getFor(ShipComponent.class);
		thrustParticleEffect = new ParticleEffect();
		thrustParticleEffect.load(Gdx.files.internal("thrust.p"), textureAtlas);
		thrustParticleEffect.setEmittersCleanUpBlendFunction(false);
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		rotationInput.getMessageDispatcher().addListener(this::handleRotationInput, InputMessage.ID);
		thrustInput.getMessageDispatcher().addListener(this::handleThrustInput, InputMessage.ID);
	}
	
	@Override
	public void removedFromEngine(Engine engine) {
		super.addedToEngine(engine);
		rotationInput.getMessageDispatcher().removeListener(this::handleRotationInput, InputMessage.ID);
		thrustInput.getMessageDispatcher().removeListener(this::handleThrustInput, InputMessage.ID);
	}
	
	private boolean handleRotationInput(Telegram message) {
		InputMessage input = (InputMessage) message.extraInfo;
		getEntities().forEach(entity -> {
			BodyComponent body = bm.get(entity);
			ShipComponent ship = sm.get(entity);
			turnShip(body, ship, input.getValue());
		});
		return true;
	}
	
	private boolean handleThrustInput(Telegram message){
		InputMessage input = (InputMessage) message.extraInfo;
		getEntities().forEach(entity ->	moveShipForward(input.getValue(), bm.get(entity), tm.get(entity), sm.get(entity)));
		return true;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		BodyComponent body = bm.get(entity);
		ShipComponent ship = sm.get(entity);

		clampVelocity(body, ship);
	}
	
	private void clampVelocity(BodyComponent body, ShipComponent ship) {
		body.setAngleVelocity(MathUtils.clamp(body.getAngleVelocity(),
				-ship.getMaxAngleSpeed(), ship.getMaxAngleSpeed()));
		body.getVelocity().clamp(0, ship.getMaxSpeed());
	}

	private void turnShip(BodyComponent body, ShipComponent ship, float direction){
		if (body.getAngleVelocity() > 0)
			PhysicsSystem.applyTorque(body, direction * 2 * ship.getAngleAcceleration());
		else
			PhysicsSystem.applyTorque(body, direction * ship.getAngleAcceleration());

		PhysicsSystem.applyForce(body,
				body.getVelocity().cpy().nor().scl(-0.1f * ship.getAcceleration()));
			
	}

	private void moveShipForward(float inputValue, BodyComponent body,
			TransformComponent transform, ShipComponent ship) {
		Vector2 inputAcceleration = CalculateUnitVectorWithAngle(
				transform.getAngle())
				.scl(ship.getAcceleration())
				.scl(inputValue);
		
		PhysicsSystem.applyForce(body, inputAcceleration);
		PhysicsSystem.applyTorque(body, Math.signum(body.getAngleVelocity())
				* (-0.1f * ship.getAngleAcceleration()));

		if(inputValue > 0)
			particleSystem.addParticleEffect(thrustParticleEffect, transform.getPosition(), transform.getAngle() + MathUtils.PI);
	}

	private static Vector2 CalculateUnitVectorWithAngle(float degrees) {
		return new Vector2(1, 0).rotate(degrees);
	}
}
