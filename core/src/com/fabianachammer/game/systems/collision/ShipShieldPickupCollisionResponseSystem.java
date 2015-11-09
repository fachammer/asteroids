package com.fabianachammer.game.systems.collision;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.fabianachammer.game.components.CollisionFlags;
import com.fabianachammer.game.components.PlayerPossessedComponent;
import com.fabianachammer.game.components.ShieldComponent;
import com.fabianachammer.game.components.ShieldPickupComponent;
import com.fabianachammer.game.components.TransformComponent;
import com.fabianachammer.game.factories.OrbFactory;
import com.fabianachammer.game.messages.CollisionMessage;

public class ShipShieldPickupCollisionResponseSystem extends
		CollisionResponseSystem {

	private final ComponentMapper<ShieldComponent> shieldMapper = ComponentMapper
			.getFor(ShieldComponent.class);
	private final ComponentMapper<ShieldPickupComponent> shieldPickupMapper = ComponentMapper
			.getFor(ShieldPickupComponent.class);
	private final ComponentMapper<TransformComponent> transformMapper = ComponentMapper
			.getFor(TransformComponent.class);
	private final ComponentMapper<PlayerPossessedComponent> playerPossessionMapper = ComponentMapper
			.getFor(PlayerPossessedComponent.class);
	private Sound pickupSound;

	public ShipShieldPickupCollisionResponseSystem() {
		pickupSound = Gdx.audio.newSound(Gdx.files.internal("pickup.wav"));
	}

	@Override
	protected boolean isSecondaryCollisionSubject(Entity entity) {
		return shieldPickupMapper.has(entity)
				&& COLLIDER.get(entity).getFlag() == CollisionFlags.SHIELD_PICKUP;
	}

	@Override
	protected boolean isPrimaryCollisionSubject(Entity entity) {
		return shieldMapper.has(entity)
				&& COLLIDER.get(entity).getFlag() == CollisionFlags.SHIP;
	}

	@Override
	protected boolean handleCollision(CollisionMessage collision) {
		ShieldPickupComponent shieldPickup = shieldPickupMapper
				.get(collision.other);
		ShieldComponent shield = shieldMapper.get(collision.self);
		shield.setMaxShield(shield.getMaxShield()
				+ shieldPickup.getShieldIncrease());
		shield.setRegenerationCooldownTimer(0);
		engine.removeEntity(collision.other);
		engine.addEntity(OrbFactory.createOrb(
				transformMapper.get(collision.other).getPosition(),
				collision.self, playerPossessionMapper.get(collision.self).getPlayer()));
		pickupSound.play(1f, 1.4f, 0);
		return true;
	}
}
