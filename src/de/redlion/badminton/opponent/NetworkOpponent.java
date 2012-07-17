package de.redlion.badminton.opponent;

import com.badlogic.gdx.math.Vector3;

import de.redlion.badminton.Birdie;
import de.redlion.badminton.Player;
import de.redlion.badminton.Resources;
import de.redlion.badminton.Player.SIDE;
import de.redlion.badminton.Player.STATE;

public class NetworkOpponent extends Opponent {

	public NetworkOpponent(SIDE side) {
		super(side);
	}

	public void update(Vector3 playerposition) {
		super.update();
	}

}
