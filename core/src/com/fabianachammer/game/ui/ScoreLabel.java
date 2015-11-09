package com.fabianachammer.game.ui;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.fabianachammer.game.components.Player;
import com.fabianachammer.game.messages.ScoreChangeMessage;

public class ScoreLabel extends Label {

	private static final BitmapFont font = new BitmapFont();
	
	public ScoreLabel(Player player) {
		super("", new LabelStyle(font, Color.WHITE));
		player.getMessageDispatcher().addListener(this::handleScoreChange, ScoreChangeMessage.ID);
		setScore(player.getScore());
	}
	
	private void setScore(int score){
		setText("Score: " + score);
	}
	
	private boolean handleScoreChange(Telegram message){
		ScoreChangeMessage scoreChange = (ScoreChangeMessage) message.extraInfo;
		setScore(scoreChange.getNewScore());
		return true;
	}
}
