package com.fabianachammer.game.components;

import com.badlogic.ashley.core.Component;

public class ShipComponent extends Component {

	private float acceleration = 40f;
	private float angleAcceleration = 800f;

	private float maxSpeed = 60f;
	private float maxAngleSpeed = 200f;
	
	public float getAcceleration() {
		return acceleration;
	}
	
	public ShipComponent setAcceleration(float acceleration) {
		this.acceleration = acceleration;
		return this;
	}
	
	public float getAngleAcceleration() {
		return angleAcceleration;
	}
	
	public ShipComponent setAngleAcceleration(float angleAcceleration) {
		this.angleAcceleration = angleAcceleration;
		return this;
	}
	
	public float getMaxSpeed() {
		return maxSpeed;
	}
	
	public ShipComponent setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
		return this;
	}
	
	public float getMaxAngleSpeed() {
		return maxAngleSpeed;
	}
	
	public ShipComponent setMaxAngleSpeed(float maxAngleSpeed) {
		this.maxAngleSpeed = maxAngleSpeed;
		return this;
	}
}
