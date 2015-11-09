package com.fabianachammer.game.factories;

import java.util.EnumSet;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.fabianachammer.game.components.BodyComponent;
import com.fabianachammer.game.components.CircleColliderComponent;
import com.fabianachammer.game.components.CollisionFlags;
import com.fabianachammer.game.components.ShapeComponent;
import com.fabianachammer.game.components.TransformComponent;

public class AsteroidGenerator {

	public static Entity[] generateAsteroids(RandomXS128 random, int amount,
			AsteroidGenerationData parameterObject) {
		Entity[] asteroids = new Entity[amount];
		for (int i = 0; i < amount; i++) {
			Entity asteroid = AsteroidGenerator.generateAsteroid(random,
					parameterObject.maxAsteroidDistance,
					parameterObject.maxAsteroidSpeed,
					parameterObject.maxAsteroidAngleSpeed);
			asteroids[i] = asteroid;
		}
		return asteroids;
	}

	public static Entity generateAsteroid(RandomXS128 random,
			float maxDistance, float maxSpeed, float maxAngleSpeed) {
		Entity asteroid = new Entity();

		TransformComponent transform = new TransformComponent()
			.setPosition(new Vector2(
				random.nextFloat() * maxDistance - maxDistance / 2,
				random.nextFloat() * maxDistance - maxDistance / 2))
			.setUniformScale(8)
			.setAngle(random.nextFloat() * 360);
		asteroid.add(transform);

		asteroid.add(new BodyComponent()
			.setVelocity(new Vector2(
							random.nextFloat() * 2 * maxSpeed - maxSpeed,
							random.nextFloat() * 2 * maxSpeed - maxSpeed))
			.setMass(transform.getScale().x)
			.setDrag(0)
			.setAngleDrag(0)
			.setAngleVelocity(random.nextFloat() * 2 * maxAngleSpeed - maxAngleSpeed));

		ShapeComponent shape = new ShapeComponent()
			.setColor(generateAsteroidColor(random))
			.setVertices(generateAsteroidVertices(random, random.nextFloat() + 1));
		asteroid.add(shape);

		asteroid.add(CircleColliderComponent
				.createFromShape(shape)
				.setFlag(CollisionFlags.ASTEROID)
				.setMask(EnumSet.of(CollisionFlags.SHIP, CollisionFlags.BULLET, CollisionFlags.ORB)));

		return asteroid;
	}

	private static Color generateAsteroidColor(RandomXS128 random) {
		float baseColor = random.nextFloat() * 0.3f + 0.3f;
		float redShift = random.nextFloat() * 0.4f;
		float greenShift = random.nextFloat() * 0.4f;
		float blueShift = random.nextFloat() * 0.4f;
		Color asteroidColor = new Color(baseColor + redShift, baseColor
				+ greenShift, baseColor + blueShift, 1f);
		return asteroidColor;
	}

	private static Vector2[] generateAsteroidVertices(RandomXS128 random, float radius) {
		Vector2[] vertices = new Vector2[10];
		for (int i = 0; i < vertices.length; i++) {
			float vertexSegmentAngle = (i / (float) vertices.length) * 360f;
			vertices[i] = new Vector2(radius * (random.nextFloat() * 0.5f + 0.5f), 0)
					.rotate(vertexSegmentAngle);
		}
		return vertices;
	}
}
