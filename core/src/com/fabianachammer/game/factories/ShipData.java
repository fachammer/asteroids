package com.fabianachammer.game.factories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class ShipData {
	public Color color;
	public Vector2[] vertices;
	public float mass;
	public float drag;
	public float angleDrag;
	public float acceleration;
	public float angleAcceleration;
	public float maxSpeed;
	public float maxAngleSpeed;
	public float fireRate;
	public Vector2 gunOffset;
	public float bulletSpeed;
	public float bulletLifeTime;
	public float collisionRadius;

	public ShipData(Color color, Vector2[] vertices, float mass, float drag,
			float angleDrag, float acceleration, float angleAcceleration,
			float maxSpeed, float maxAngleSpeed, float fireRate,
			Vector2 gunOffset, float bulletSpeed, float bulletLifeTime, float collisionRadius) {
		this.color = color;
		this.vertices = vertices;
		this.mass = mass;
		this.drag = drag;
		this.angleDrag = angleDrag;
		this.acceleration = acceleration;
		this.angleAcceleration = angleAcceleration;
		this.maxSpeed = maxSpeed;
		this.maxAngleSpeed = maxAngleSpeed;
		this.fireRate = fireRate;
		this.gunOffset = gunOffset;
		this.bulletSpeed = bulletSpeed;
		this.bulletLifeTime = bulletLifeTime;
		this.collisionRadius = collisionRadius;
	}
}