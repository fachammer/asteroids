package com.fabianachammer.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.fabianachammer.game.components.ShapeComponent;
import com.fabianachammer.game.components.TransformComponent;

public class RenderSystem extends IteratingSystem {

	private ComponentMapper<ShapeComponent> sm;
	private ComponentMapper<TransformComponent> tm;
	private ShapeRenderer shapeRenderer;
	private Camera camera;

	public RenderSystem(Camera camera) {
		super(Family.all(ShapeComponent.class, TransformComponent.class).get());
		this.camera = camera;
		this.shapeRenderer = new ShapeRenderer();
		sm = ComponentMapper.getFor(ShapeComponent.class);
		tm = ComponentMapper.getFor(TransformComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		ShapeComponent shape = sm.get(entity);
		TransformComponent transform = tm.get(entity);

		render(shape, transform);
	}

	private void render(ShapeComponent shape, TransformComponent transform) {
		transformRenderer(transform);
		renderVertices(shape);
	}

	private void transformRenderer(TransformComponent transform) {
		shapeRenderer.identity();
		shapeRenderer.translate(transform.getPosition().x, transform.getPosition().y, 0);
		shapeRenderer.rotate(0, 0, 1, transform.getAngle());
		shapeRenderer.scale(transform.getScale().x, transform.getScale().y, 1);
	}

	private void renderVertices(ShapeComponent shape) {
		shapeRenderer.setColor(shape.getColor());
		for(int i = 0; i < shape.getVertices().length - 1; i++) {
			shapeRenderer.line(shape.getVertices()[i], shape.getVertices()[i + 1]);
		}
		shapeRenderer.line(shape.getVertices()[shape.getVertices().length - 1], shape.getVertices()[0]);
	}

	@Override
	public void update(float deltaTime) {
		clearFrame();
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Line);
		super.update(deltaTime);
		shapeRenderer.end();
	}

	private void clearFrame() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
}
