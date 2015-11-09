package com.fabianachammer.game.messages;

public class ScoreChangeMessage {

	public static final int ID = 500;
	
	private int oldScore;
	private int newScore;
	
	public ScoreChangeMessage(int oldScore, int newScore){
		this.oldScore = oldScore;
		this.newScore = newScore;
	}
	
	public int getOldScore(){
		return oldScore;
	}
	
	public int getNewScore(){
		return newScore;
	}
}
