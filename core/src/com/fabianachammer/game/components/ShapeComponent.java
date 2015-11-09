package com.fabianachammer.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class ShapeComponent extends Component {

	private Vector2[] vertices;
	private Color color = Color.WHITE;

	public Vector2[] getVertices() {
		return vertices;
	}

	public ShapeComponent setVertices(Vector2[] vertices) {
		this.vertices = vertices;
		return this;
	}

	public Color getColor() {
		return color;
	}

	public ShapeComponent setColor(Color color) {
		this.color = color;
		return this;
	}
}
