package com.fabianachammer.game.factories;

public class AsteroidGenerationData {
	public float maxAsteroidDistance;
	public float maxAsteroidSpeed;
	public float maxAsteroidAngleSpeed;

	public AsteroidGenerationData(float maxAsteroidDistance,
			float maxAsteroidSpeed, float maxAsteroidAngleSpeed) {
		this.maxAsteroidDistance = maxAsteroidDistance;
		this.maxAsteroidSpeed = maxAsteroidSpeed;
		this.maxAsteroidAngleSpeed = maxAsteroidAngleSpeed;
	}
}