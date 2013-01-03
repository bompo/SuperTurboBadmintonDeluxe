package de.redlion.badminton;

import com.badlogic.gdx.Game;

public class Badminton extends Game {
	@Override 
	public void create () {
		setScreen(new MenuScreen(this));
	}
}
