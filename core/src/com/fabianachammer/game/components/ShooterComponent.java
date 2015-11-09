package com.fabianachammer.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class ShooterComponent extends Component {

	private float fireRate = 1f;
	private Vector2 gunOffset = new Vector2();
	private float bulletSpeed = 1f;
	private float bulletLifeTime = 1f;

	private float cooldownTimer = 0f;
	
	public float getFireRate() {
		return fireRate;
	}

	public ShooterComponent setFireRate(float fireRate) {
		this.fireRate = fireRate;
		return this;
	}

	public Vector2 getGunOffset() {
		return gunOffset;
	}

	public ShooterComponent setGunOffset(Vector2 gunOffset) {
		this.gunOffset = gunOffset;
		return this;
	}

	public float getBulletSpeed() {
		return bulletSpeed;
	}

	public ShooterComponent setBulletSpeed(float bulletSpeed) {
		this.bulletSpeed = bulletSpeed;
		return this;
	}

	public float getBulletLifeTime() {
		return bulletLifeTime;
	}

	public ShooterComponent setBulletLifeTime(float bulletLifeTime) {
		this.bulletLifeTime = bulletLifeTime;
		return this;
	}

	public float getCooldownTimer() {
		return cooldownTimer;
	}

	public void setCooldownTimer(float cooldownTimer) {
		this.cooldownTimer = cooldownTimer;
	}
}
