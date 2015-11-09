package com.fabianachammer.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.fabianachammer.engine.input.InputSystem;
import com.fabianachammer.engine.input.Keyboard;
import com.fabianachammer.game.components.Player;
import com.fabianachammer.game.components.TransformComponent;
import com.fabianachammer.game.factories.AsteroidGenerationData;
import com.fabianachammer.game.factories.AsteroidGenerator;
import com.fabianachammer.game.factories.ShipData;
import com.fabianachammer.game.factories.ShipFactory;
import com.fabianachammer.game.systems.AttractionSystem;
import com.fabianachammer.game.systems.AutoDestructionSystem;
import com.fabianachammer.game.systems.CollisionShapeRenderSystem;
import com.fabianachammer.game.systems.FlockingSystem;
import com.fabianachammer.game.systems.ParticleSystem;
import com.fabianachammer.game.systems.PhysicsSystem;
import com.fabianachammer.game.systems.PipingSystem;
import com.fabianachammer.game.systems.RenderSystem;
import com.fabianachammer.game.systems.ShieldSystem;
import com.fabianachammer.game.systems.ShipMovementSystem;
import com.fabianachammer.game.systems.ShootingSystem;
import com.fabianachammer.game.systems.VictorySystem;
import com.fabianachammer.game.systems.collision.AsteroidCollisionResponseSystem;
import com.fabianachammer.game.systems.collision.BulletCollisionResponseSystem;
import com.fabianachammer.game.systems.collision.CollisionDetectionSystem;
import com.fabianachammer.game.systems.collision.OrbAsteroidCollisionResponseSystem;
import com.fabianachammer.game.systems.collision.ScoreSystem;
import com.fabianachammer.game.systems.collision.ShipAsteroidCollisionResponseSystem;
import com.fabianachammer.game.systems.collision.ShipShieldPickupCollisionResponseSystem;
import com.fabianachammer.game.ui.ScoreLabel;
import com.fabianachammer.game.ui.WinLabel;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Game extends ApplicationAdapter {
	private static final float MAX_CAMERA_LOOKAHEAD = 20f;
	private static final int ASTEROID_AMOUNT = 10;
	private static final float SHIP_MAX_ANGLE_SPEED = 300f;
	private static final float SHIP_MAX_SPEED = 30f;
	private static final float SHIP_ANGLE_ACCELERATION = 400f;
	private static final float SHIP_ACCELERATION = 40f;
	private static final Color SHIP_COLOR = Color.YELLOW;
	private static final float SHIP_ANGLE_DRAG = 0.9f;
	private static final float SHIP_DRAG = 0.2f;
	private static final float SHIP_MASS = 0.3f;
	private static final float MAX_ASTEROID_ANGLE_SPEED = 20f;
	private static final float MAX_ASTEROID_SPEED = 10f;
	private static final float MAX_ASTEROID_DISTANCE = 1000f;
	private static final float VIEW_WIDTH = 100;
	private static final Vector2[] SHIP_VERTICES = { new Vector2(-0.5f, 0.5f),
			new Vector2(-0.5f, -0.5f), new Vector2(1f, 0f) };
	private static final float SHIP_FIRE_RATE = 8f;
	private static final Vector2 SHIP_GUN_OFFSET = new Vector2(1.1f, 0f);
	private static final float SHIP_COLLISION_RADIUS = 1f;
	private static final float BULLET_SPEED = 150f;
	private static final float BULLET_LIFETIME = 0.6f;

	private OrthographicCamera camera;
	private Engine engine;
	private Stage stage;
	private TextureAtlas textureAtlas;
	
	private Player player = new Player();
	private VirtualGamepad virtualGamepad;
	
	@Override
	public void create() {
		textureAtlas = new TextureAtlas(Gdx.files.internal("hud.pack"));
		camera = new OrthographicCamera();
		engine = new Engine();

		Entity ship = createShip();
		
		setupInput();
		setupSystems(engine, camera, ship, MAX_CAMERA_LOOKAHEAD, textureAtlas, virtualGamepad);
		setupEntities(engine, ship, createAsteroids());
		setupHUD();
	}

	private Entity createShip() {
		ShipData shipData = new ShipData(SHIP_COLOR, SHIP_VERTICES, SHIP_MASS,
				SHIP_DRAG, SHIP_ANGLE_DRAG, SHIP_ACCELERATION,
				SHIP_ANGLE_ACCELERATION, SHIP_MAX_SPEED, SHIP_MAX_ANGLE_SPEED,
				SHIP_FIRE_RATE, SHIP_GUN_OFFSET, BULLET_SPEED, BULLET_LIFETIME,
				SHIP_COLLISION_RADIUS);
		Entity ship = ShipFactory.createShip(shipData, player);
		ship.getComponent(TransformComponent.class).setPosition(new Vector2(
				VIEW_WIDTH / 2, VIEW_WIDTH * Gdx.graphics.getHeight()
						/ Gdx.graphics.getWidth() / 2));
		return ship;
	}

	private Entity[] createAsteroids() {
		RandomXS128 random = new RandomXS128();
		AsteroidGenerationData asteroidGenerationData = new AsteroidGenerationData(
				MAX_ASTEROID_DISTANCE, MAX_ASTEROID_SPEED,
				MAX_ASTEROID_ANGLE_SPEED);
		Entity[] asteroids = AsteroidGenerator.generateAsteroids(random,
				ASTEROID_AMOUNT, asteroidGenerationData);
		return asteroids;
	}
	
	private void setupInput(){
		Keyboard keyboard = new Keyboard();
		virtualGamepad = new VirtualGamepad(keyboard);
		Gdx.input.setInputProcessor(keyboard);
	}
	
	private void setupHUD() {
		stage = new Stage(new ScreenViewport());
		
		ScoreLabel scoreLabel = new ScoreLabel(player);
		scoreLabel.setPosition(20, stage.getHeight() - 20);
		stage.addActor(scoreLabel);
		
		WinLabel winLabel = new WinLabel();
		winLabel.setPosition((stage.getWidth() - winLabel.getWidth()) / 2, (stage.getHeight() - winLabel.getHeight()) / 2);
		stage.addActor(winLabel);
	}

	private static void setupSystems(Engine engine, Camera camera,
			Entity cameraTarget, float maxCameraLookahead, TextureAtlas textureAtlas, VirtualGamepad virtualGamepad) {
		InputSystem inputSystem = new InputSystem();
		inputSystem.addInputSource(virtualGamepad.getRotationInput());
		inputSystem.addInputSource(virtualGamepad.getThrustInput());
		inputSystem.addInputSource(virtualGamepad.getShootInput());

		float worldWidth = VIEW_WIDTH;
		float worldHeight = VIEW_WIDTH * Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
		
		ParticleSystem particleSystem = new ParticleSystem(camera);
		engine.addSystem(inputSystem);
		engine.addSystem(new AutoDestructionSystem(worldWidth, worldHeight));
		engine.addSystem(new ShipMovementSystem(virtualGamepad, particleSystem, textureAtlas));
		engine.addSystem(new ShootingSystem(virtualGamepad));
		engine.addSystem(new AttractionSystem());
		engine.addSystem(new FlockingSystem());
		engine.addSystem(new PhysicsSystem());
		engine.addSystem(new PipingSystem(worldWidth, worldHeight));
		engine.addSystem(new CollisionDetectionSystem());
		engine.addSystem(new AsteroidCollisionResponseSystem(4, particleSystem, textureAtlas));
		engine.addSystem(new BulletCollisionResponseSystem(particleSystem, textureAtlas));
		engine.addSystem(new ShieldSystem());
		engine.addSystem(new ShipAsteroidCollisionResponseSystem());
		engine.addSystem(new ShipShieldPickupCollisionResponseSystem());
		engine.addSystem(new OrbAsteroidCollisionResponseSystem(particleSystem, textureAtlas));
		engine.addSystem(new ScoreSystem(100f));
		engine.addSystem(new VictorySystem());
		engine.addSystem(new RenderSystem(camera));
		engine.addSystem(particleSystem);
		engine.addSystem(new CollisionShapeRenderSystem(camera));
	}

	private static void setupEntities(Engine engine, Entity ship,
			Entity[] asteroids) {
		engine.addEntity(ship);
		for (Entity asteroid : asteroids)
			engine.addEntity(asteroid);
	}

	@Override
	public void render() {
		float dt = Gdx.graphics.getDeltaTime();
		engine.update(dt);
		stage.act(dt);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		float aspectRatio = width / (float) height;
		camera.setToOrtho(false, VIEW_WIDTH, VIEW_WIDTH / aspectRatio);
		stage.getViewport().update(width, height, true);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		stage.dispose();
		textureAtlas.dispose();
	}
}
