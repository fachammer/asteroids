package com.fabianachammer.game.components;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.fabianachammer.game.messages.ScoreChangeMessage;

public class Player {

	private static int totalId;
	
	private int score = 0;
	private int id;
	private final MessageDispatcher messageDispatcher = new MessageDispatcher();
	
	public Player(){
		this.id = totalId++;
	}
	
	public int getScore(){
		return score;
	}
	
	public int getId(){
		return id;
	}
	
	public Player changeScore(int change){
		int oldScore = score;
		score += change;
		
		if(score < 0)
			score = 0;
		
		messageDispatcher.dispatchMessage(null, ScoreChangeMessage.ID, new ScoreChangeMessage(oldScore, score));
		
		return this;
	}
	
	public MessageDispatcher getMessageDispatcher(){
		return messageDispatcher;
	}
}
