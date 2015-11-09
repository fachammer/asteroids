package com.fabianachammer.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.math.Vector2;

public class BodyComponent extends Component {

	private Vector2 velocity = new Vector2();
	private float angleVelocity = 0f;
	
	private Vector2 acceleration = new Vector2();
	private float angleAcceleration = 0f;
	
	private float mass = 1f;
	private float drag = 0f;
	private float angleDrag = 0f;

	private final MessageDispatcher messageDispatcher= new MessageDispatcher();
	
	public MessageDispatcher getMessageDispatcher(){
		return messageDispatcher;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public BodyComponent setVelocity(Vector2 velocity) {
		this.velocity = velocity;
		return this;
	}

	public float getAngleVelocity() {
		return angleVelocity;
	}

	public BodyComponent setAngleVelocity(float angleVelocity) {
		this.angleVelocity = angleVelocity;
		return this;
	}
	
	public Vector2 getAcceleration() {
		return acceleration;
	}

	public BodyComponent setAcceleration(Vector2 acceleration) {
		this.acceleration = acceleration;
		return this;
	}

	public float getAngleAcceleration() {
		return angleAcceleration;
	}

	public BodyComponent setAngleAcceleration(float angleAcceleration) {
		this.angleAcceleration = angleAcceleration;
		return this;
	}

	public float getMass() {
		return mass;
	}

	public BodyComponent setMass(float mass) {
		this.mass = mass;
		return this;
	}

	public float getDrag() {
		return drag;
	}

	public BodyComponent setDrag(float drag) {
		this.drag = drag;
		return this;
	}

	public float getAngleDrag() {
		return angleDrag;
	}

	public BodyComponent setAngleDrag(float angleDrag) {
		this.angleDrag = angleDrag;
		return this;
	}
}
