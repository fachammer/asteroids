package com.fabianachammer.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class FlockingComponent extends Component {

	private Entity target;
	private float targetAttraction;
	private float tangentialAttraction;
	private float repulsion;
	private float repulsionThreshold;
	private float cohesion;
	private float cohesionThreshold;
	private float alignment;
	private float alignmentThreshold;

	public Entity getTarget() {
		return target;
	}

	public FlockingComponent setTarget(Entity target) {
		this.target = target;
		return this;
	}

	public float getTargetAttraction() {
		return targetAttraction;
	}
	
	public FlockingComponent setTargetAttraction(float targetAttraction){
		this.targetAttraction = targetAttraction;
		return this;
	}
	
	public float getRepulsion(){
		return repulsion;
	}
	
	public FlockingComponent setRepulsion(float repulsion){
		this.repulsion = repulsion;
		return this;
	}

	public float getRepulsionThreshold() {
		return repulsionThreshold;
	}
	
	public FlockingComponent setRepulsionThreshold(float repulsionThreshold) {
		this.repulsionThreshold = repulsionThreshold;
		return this;
	}

	public float getCohesion() {
		return cohesion;
	}
	
	public FlockingComponent setCohesion(float cohesion){
		this.cohesion = cohesion;
		return this;
	} 
	
	public float getCohesionThreshold() {
		return cohesionThreshold;
	}
	
	public FlockingComponent setCohesionThreshold(float cohesionThreshold) {
		this.cohesionThreshold = cohesionThreshold;
		return this;
	}
	
	public float getAlignment() {
		return alignment;
	}
	
	public FlockingComponent setAlignment(float alignment){
		this.alignment = alignment;
		return this;
	} 
	
	public float getAlignmentThreshold() {
		return alignmentThreshold;
	}
	
	public FlockingComponent setAlignmentThreshold(float alignmentThreshold) {
		this.alignmentThreshold = alignmentThreshold;
		return this;
	}
	
	public float getTangentialAttraction(){
		return tangentialAttraction;
	}
	
	public FlockingComponent setTangentialAttraction(float tangentialAttraction){
		this.tangentialAttraction = tangentialAttraction;
		return this;
	}
}
