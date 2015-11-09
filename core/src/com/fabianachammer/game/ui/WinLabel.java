package com.fabianachammer.game.ui;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.fabianachammer.game.systems.VictorySystem;

public class WinLabel extends Label {

	private static final BitmapFont FONT = new BitmapFont();
	
	public WinLabel(){
		super("You won!", new LabelStyle(FONT, Color.WHITE));
		setVisible(false);
		MessageManager.getInstance().addListener(this::handleVictoryMessage, VictorySystem.VICTORY_MESSAGE_ID);
	}
	
	private boolean handleVictoryMessage(Telegram message){
		setVisible(true);
		return true;
	}
}
