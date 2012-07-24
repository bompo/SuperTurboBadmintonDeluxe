package de.redlion.badminton.opponent;

import com.badlogic.gdx.math.Vector3;

import de.redlion.badminton.controls.PlayerTwoControlMappings;

public class LocalOpponent extends Opponent {
	
	public LocalOpponent(SIDE side) {
		super(side);
		this.input = new PlayerTwoControlMappings();
	}

	public void update(Vector3 playerposition) {
		super.update();
	}

}
