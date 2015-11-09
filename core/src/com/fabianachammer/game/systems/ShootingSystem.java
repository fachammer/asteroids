package com.fabianachammer.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.fabianachammer.engine.input.InputSource;
import com.fabianachammer.game.VirtualGamepad;
import com.fabianachammer.game.components.PlayerPossessedComponent;
import com.fabianachammer.game.components.ShooterComponent;
import com.fabianachammer.game.components.TransformComponent;
import com.fabianachammer.game.factories.BulletFactory;
import com.fabianachammer.game.messages.InputMessage;

public class ShootingSystem extends IteratingSystem {

	private static final RandomXS128 RANDOM = new RandomXS128();
	
	private Engine engine;
	private final ComponentMapper<ShooterComponent> shooterMapper = ComponentMapper
			.getFor(ShooterComponent.class);
	private final ComponentMapper<TransformComponent> transformMapper = ComponentMapper
			.getFor(TransformComponent.class);
	private final ComponentMapper<PlayerPossessedComponent> playerPossesionMapper = ComponentMapper
			.getFor(PlayerPossessedComponent.class);
	private InputSource shootInput;
	private Sound shootSound;

	public ShootingSystem(VirtualGamepad virtualGamepad) {
		super(Family.all(TransformComponent.class, ShooterComponent.class)
				.get());
		this.shootInput = virtualGamepad.getShootInput();
		shootSound = Gdx.audio.newSound(Gdx.files.internal("shoot.wav"));
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		this.engine = engine;
		this.shootInput.getMessageDispatcher().addListener(
				this::handleShootInput, InputMessage.ID);
	}

	private boolean handleShootInput(Telegram message) {
		InputMessage input = (InputMessage) message.extraInfo;
		if (input.getValue() > 0) {
			getEntities().forEach(
					entity -> shoot(transformMapper.get(entity),
							shooterMapper.get(entity),
							playerPossesionMapper.get(entity)));
		}
		return true;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		ShooterComponent shooter = shooterMapper.get(entity);

		if (shooter.getCooldownTimer() > 0f)
			shooter.setCooldownTimer(shooter.getCooldownTimer() - deltaTime);
		else
			shooter.setCooldownTimer(0f);
	}

	private static float nextRandomFloatInRange(float min, float max){
		return RANDOM.nextFloat() * (max - min) + min;
	}
	
	private void shoot(TransformComponent transform, ShooterComponent shooter,
			PlayerPossessedComponent playerPossesion) {
		if (shooter.getCooldownTimer() > 0f)
			return;

		Vector2 bulletVelocity = new Vector2(1, 0).rotate(transform.getAngle())
				.scl(shooter.getBulletSpeed());
		Entity bullet = BulletFactory.createBullet(
				transform
						.getPosition()
						.cpy()
						.add(shooter.getGunOffset().cpy()
								.rotate(transform.getAngle())), bulletVelocity,
				shooter.getBulletLifeTime(), playerPossesion.getPlayer());

		shooter.setCooldownTimer(1f / shooter.getFireRate());
		engine.addEntity(bullet);
		shootSound.play(0.6f, nextRandomFloatInRange(1.35f, 1.65f), 0);
	}
}
