package de.redlion.badminton.controls;

import com.badlogic.gdx.Input;

public class PlayerTwoControlMappings extends ControlMappings {
	
	public PlayerTwoControlMappings() {
		this.up = Input.Keys.UP;
		this.down = Input.Keys.DOWN;
		this.left = Input.Keys.LEFT;
		this.right = Input.Keys.RIGHT;
		this.shoot = Input.Keys.SHIFT_RIGHT;
		this.smash = Input.Keys.CONTROL_RIGHT;
	}

}
