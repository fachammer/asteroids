package com.fabianachammer.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.fabianachammer.game.components.BodyComponent;
import com.fabianachammer.game.components.FlockingComponent;
import com.fabianachammer.game.components.TransformComponent;

public class FlockingSystem extends IteratingSystem {

	private ComponentMapper<FlockingComponent> flockingMapper;
	private ComponentMapper<BodyComponent> bodyMapper;
	private ComponentMapper<TransformComponent> transformMapper;
	
	// made variable to avoid creating a new vector each iteration
	private Vector2 desiredVelocity = new Vector2();
	
	public FlockingSystem() {
		super(Family.all(FlockingComponent.class, BodyComponent.class, TransformComponent.class).get());
		flockingMapper = ComponentMapper.getFor(FlockingComponent.class);
		bodyMapper = ComponentMapper.getFor(BodyComponent.class);
		transformMapper = ComponentMapper.getFor(TransformComponent.class);
	}
	
	@Override
	protected void processEntity(Entity self, float deltaTime) {
		desiredVelocity.setZero();
		desiredVelocity.add(calculateTargetAttractionForce(self));
		desiredVelocity.add(calculateTangentialForce(self));
		desiredVelocity.add(calculateCohesionForce(self, getEntities()));
		desiredVelocity.add(calculateRepulsionForce(self, getEntities()));
		desiredVelocity.add(calculateAlignmentForce(self, getEntities()));
		BodyComponent body = bodyMapper.get(self);
		PhysicsSystem.applyForce(body, desiredVelocity.nor().scl(100f).sub(body.getVelocity()).clamp(0f, 100f));
	}
	
	private Vector2 calculateTargetAttractionForce(Entity entity){
		FlockingComponent flocking = flockingMapper.get(entity);
		TransformComponent transform = transformMapper.get(entity);
		if(flocking.getTarget() == null)
			return Vector2.Zero;
		
		TransformComponent targetTransform = transformMapper.get(flocking.getTarget());
		
		return targetTransform.getPosition().cpy().sub(transform.getPosition()).nor().scl(flocking.getTargetAttraction());
	}
	
	private Vector2 calculateTangentialForce(Entity entity){
		FlockingComponent flocking = flockingMapper.get(entity);
		TransformComponent transform = transformMapper.get(entity);
		if(flocking.getTarget() == null)
			return Vector2.Zero;
		
		TransformComponent targetTransform = transformMapper.get(flocking.getTarget());
		
		return targetTransform.getPosition().cpy().sub(transform.getPosition()).rotate90(-1).nor().scl(flocking.getTangentialAttraction());
	}
	
	private float square(float value){
		return value * value;
	}
	
	private Vector2 calculateRepulsionForce(Entity self, Iterable<Entity> allEntities){
		FlockingComponent selfFlocking = flockingMapper.get(self);
		TransformComponent selfTransform = transformMapper.get(self);
		
		Vector2 repulsionForce = new Vector2();
		int otherEntityCount = 0;
		for(Entity other : allEntities){
			if(other == self)
				continue;
			
			TransformComponent otherTransform = transformMapper.get(other);
			Vector2 deltaLocation = selfTransform.getPosition().cpy().sub(otherTransform.getPosition());
			if(deltaLocation.len2() < square(selfFlocking.getRepulsionThreshold())){
				repulsionForce.add(deltaLocation.nor());
			}
			
			otherEntityCount++;
		}
		
		
		if(selfFlocking.getTarget() != null){
			TransformComponent targetTransform = transformMapper.get(selfFlocking.getTarget());
			Vector2 deltaLocation = selfTransform.getPosition().cpy().sub(targetTransform.getPosition());
			if(deltaLocation.len2() < square(selfFlocking.getRepulsionThreshold()))
				repulsionForce.add(deltaLocation.nor().scl(5));
			
			otherEntityCount++;
		}

		if(otherEntityCount > 0)
			repulsionForce.scl(1f / otherEntityCount);
		
		return repulsionForce.nor().scl(selfFlocking.getRepulsion());
	}
	
	private float distanceSquared(Entity self, Entity other){
		return transformMapper.get(self).getPosition().cpy().sub(transformMapper.get(other).getPosition()).len2();
	}
	
	private Vector2 calculateAveragePosition(Entity self, Iterable<Entity> entities){
		FlockingComponent selfFlocking = flockingMapper.get(self);
		Vector2 averagePosition = new Vector2();
		int entityCount = 0;
		for(Entity other : entities){
			if(distanceSquared(self, other) < square(selfFlocking.getCohesionThreshold())){
				averagePosition.add(transformMapper.get(other).getPosition());
				entityCount++;
			}
		}

		return averagePosition.scl(1f / entityCount);
	}
	
	private Vector2 calculateCohesionForce(Entity self, Iterable<Entity> allEntities){
		TransformComponent selfTransform = transformMapper.get(self);
		FlockingComponent selfFlocking = flockingMapper.get(self);
		
		Vector2 deltaLocation = calculateAveragePosition(self, allEntities).sub(selfTransform.getPosition());
		return deltaLocation.nor().scl(selfFlocking.getCohesion());
	}
	
	private Vector2 calculateAverageVelocity(Entity self, Iterable<Entity> entities){
		FlockingComponent selfFlocking = flockingMapper.get(self);
		Vector2 averageVelocity = new Vector2();
		int entityCount = 0;
		for(Entity other : entities){
			if(distanceSquared(self, other) < square(selfFlocking.getAlignmentThreshold())){
				averageVelocity.add(bodyMapper.get(other).getVelocity());
				entityCount++;
			}
		}

		return averageVelocity.scl(1f / entityCount);
	}
	
	private Vector2 calculateAlignmentForce(Entity self, Iterable<Entity> entities){
		Vector2 deltaVelocity = calculateAverageVelocity(self, entities).sub(bodyMapper.get(self).getVelocity());
		return deltaVelocity.nor().scl(flockingMapper.get(self).getAlignment());
	}
}
