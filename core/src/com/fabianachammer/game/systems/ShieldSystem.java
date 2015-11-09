package com.fabianachammer.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.fabianachammer.game.components.ShieldComponent;
import com.fabianachammer.game.messages.ShieldChangeMessage;

public class ShieldSystem extends IteratingSystem {

	private ComponentMapper<ShieldComponent> sm;

	public ShieldSystem() {
		super(Family.all(ShieldComponent.class).get());
		sm = ComponentMapper.getFor(ShieldComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		ShieldComponent shield = sm.get(entity);

		if(shield.getCurrentShield() >= shield.getMaxShield())
			return;
		
		if (shield.getRegenerationCooldownTimer() > 0)
			shield.setRegenerationCooldownTimer(shield
					.getRegenerationCooldownTimer() - deltaTime);
		else 
			changeShield(shield, shield.getRegenerationRate() * deltaTime);
	}
	
	public static void changeShield(ShieldComponent shield, float change){
		if(change < 0)
			shield.setRegenerationCooldownTimer(shield.getRegenerationCooldown());
		
		float oldShield = shield.getCurrentShield();
		shield.setCurrentShield(shield.getCurrentShield() + change);
		shield.setCurrentShield(MathUtils.clamp(shield.getCurrentShield(), 0, shield.getMaxShield()));
		shield.getMessageDispatcher().dispatchMessage(null, ShieldChangeMessage.ID, 
				new ShieldChangeMessage(shield, oldShield, shield.getCurrentShield()));
	}
}
