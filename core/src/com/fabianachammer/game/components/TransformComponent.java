package com.fabianachammer.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class TransformComponent extends Component {

	private Vector2 position = new Vector2();
	private float angle = 0f;
	private Vector2 scale = new Vector2(1, 1);

	public Vector2 getPosition() {
		return position;
	}

	public TransformComponent setPosition(Vector2 position) {
		this.position = position;
		return this;
	}

	public float getAngle() {
		return angle;
	}

	public TransformComponent setAngle(float angle) {
		this.angle = angle;
		return this;
	}

	public Vector2 getScale() {
		return scale;
	}

	public TransformComponent setScale(Vector2 scale) {
		this.scale = scale;
		return this;
	}
	
	public TransformComponent setUniformScale(float scale){
		this.scale = new Vector2(scale, scale);
		return this;
	}
}
