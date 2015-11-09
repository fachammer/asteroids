package com.fabianachammer.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.msg.MessageDispatcher;

public class ShieldComponent extends Component {

	private float maxShield;
	private float currentShield;
	
	private float regenerationRate;
	private float regenerationCooldown;
	
	private float regenerationCooldownTimer;
	
	private final MessageDispatcher messageDispatcher= new MessageDispatcher();
	
	public MessageDispatcher getMessageDispatcher(){
		return messageDispatcher;
	}

	public float getMaxShield() {
		return maxShield;
	}

	public ShieldComponent setMaxShield(float maxShield) {
		this.maxShield = maxShield;
		return this;
	}

	public float getCurrentShield() {
		return currentShield;
	}

	public ShieldComponent setCurrentShield(float currentShield) {
		this.currentShield = currentShield;
		return this;
	}

	public float getRegenerationRate() {
		return regenerationRate;
	}

	public ShieldComponent setRegenerationRate(float regenerationRate) {
		this.regenerationRate = regenerationRate;
		return this;
	}

	public float getRegenerationCooldown() {
		return regenerationCooldown;
	}

	public ShieldComponent setRegenerationCooldown(float regenerationCooldown) {
		this.regenerationCooldown = regenerationCooldown;
		return this;
	}

	public float getRegenerationCooldownTimer() {
		return regenerationCooldownTimer;
	}

	public void setRegenerationCooldownTimer(float regenerationCooldownTimer) {
		this.regenerationCooldownTimer = regenerationCooldownTimer;
	}
}
