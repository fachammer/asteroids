package com.fabianachammer.game.components;

import java.util.EnumSet;

import com.badlogic.ashley.core.Component;

public class AttractionComponent extends Component {

	private float attractionForce;
	private float attractionThreshold;
	
	private CollisionFlags attractionFlag;
	private EnumSet<CollisionFlags> attractionMask;
	
	public float getAttractionForce() {
		return attractionForce;
	}
	
	public AttractionComponent setAttractionForce(float attractionForce) {
		this.attractionForce = attractionForce;
		return this;
	}
	
	public float getAttractionThreshold() {
		return attractionThreshold;
	}

	public AttractionComponent setAttractionThreshold(float attractionThreshold) {
		this.attractionThreshold = attractionThreshold;
		return this;
	}

	public CollisionFlags getAttractionFlag() {
		return attractionFlag;
	}
	
	public AttractionComponent setAttractionFlag(CollisionFlags attractionFlag) {
		this.attractionFlag = attractionFlag;
		return this;
	}
	
	public EnumSet<CollisionFlags> getAttractionMask() {
		return attractionMask;
	}
	
	public AttractionComponent setAttractionMask(EnumSet<CollisionFlags> attractionMask) {
		this.attractionMask = attractionMask;
		return this;
	}
}
