package com.fabianachammer.game.components;

import com.badlogic.ashley.core.Component;

public class PlayerPossessedComponent extends Component {
	
	private Player player;
	
	public PlayerPossessedComponent(Player player){
		this.player = player;
	}
	
	public Player getPlayer(){ 
		return player;
	}
	
	public PlayerPossessedComponent setPlayer(Player player){
		this.player = player;
		return this;
	}
}
