package com.fabianachammer.game.components;

import java.util.EnumSet;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.math.Vector2;

public class CircleColliderComponent extends Component {

	private float radius;
	
	private CollisionFlags flag;
	private EnumSet<CollisionFlags> mask;
	
	private final MessageDispatcher messageDispatcher = new MessageDispatcher();
	
	public MessageDispatcher getMessageDispatcher(){
		return messageDispatcher;
	}

	public float getRadius() {
		return radius;
	}

	public CircleColliderComponent setRadius(float radius) {
		this.radius = radius;
		return this;
	}

	public CollisionFlags getFlag() {
		return flag;
	}

	public CircleColliderComponent setFlag(CollisionFlags flag) {
		this.flag = flag;
		return this;
	}

	public EnumSet<CollisionFlags> getMask() {
		return mask;
	}

	public CircleColliderComponent setMask(EnumSet<CollisionFlags> mask) {
		this.mask = mask;
		return this;
	}

	public static CircleColliderComponent createFromShape(
			ShapeComponent shape) {
		Vector2 averagePosition = new Vector2();
	
		for (Vector2 vertex : shape.getVertices()) {
			averagePosition.add(vertex);
		}
		averagePosition.scl(1f / shape.getVertices().length);
	
		float radius = 0f;
		for (Vector2 vertex : shape.getVertices()) {
			float vertexDistance = vertex.dst(averagePosition);
			if (vertexDistance > radius)
				radius = vertexDistance;
		}
	
		CircleColliderComponent circleCollider = new CircleColliderComponent();
		circleCollider.radius = radius;
		circleCollider.flag = CollisionFlags.ASTEROID;
		circleCollider.mask = EnumSet.of(CollisionFlags.BULLET, CollisionFlags.SHIP);
		return circleCollider;
	}
}