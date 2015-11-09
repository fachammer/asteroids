package com.fabianachammer.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.fabianachammer.game.components.CircleColliderComponent;
import com.fabianachammer.game.components.TransformComponent;

public class CollisionShapeRenderSystem extends IteratingSystem {
	private ComponentMapper<CircleColliderComponent> cm;
	private ComponentMapper<TransformComponent> tm;
	private ShapeRenderer shapeRenderer;
	private Camera camera;
	private boolean shouldDisplayCollisionShape = false;

	public CollisionShapeRenderSystem(Camera camera) {
		super(Family.all(CircleColliderComponent.class, TransformComponent.class).get());
		this.camera = camera;
		this.shapeRenderer = new ShapeRenderer();
		cm = ComponentMapper.getFor(CircleColliderComponent.class);
		tm = ComponentMapper.getFor(TransformComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		CircleColliderComponent circleCollider = cm.get(entity);
		TransformComponent transform = tm.get(entity);

		render(circleCollider, transform);
	}

	private void render(CircleColliderComponent circleCollider, TransformComponent transform) {
		transformRenderer(transform);
		renderVertices(circleCollider);
	}

	private void transformRenderer(TransformComponent transform) {
		shapeRenderer.identity();
		shapeRenderer.translate(transform.getPosition().x, transform.getPosition().y, 0);
		shapeRenderer.rotate(0, 0, 1, transform.getAngle());
		shapeRenderer.scale(transform.getScale().x, transform.getScale().y, 1);
	}

	private void renderVertices(CircleColliderComponent circleCollider) {
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.circle(0, 0, circleCollider.getRadius(), 20);
	}

	@Override
	public void update(float deltaTime) {
		if(Gdx.input.isKeyJustPressed(Keys.C))
			shouldDisplayCollisionShape = !shouldDisplayCollisionShape;
		
		if(!shouldDisplayCollisionShape)
			return;
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Line);
		super.update(deltaTime);
		shapeRenderer.end();
	}
}
