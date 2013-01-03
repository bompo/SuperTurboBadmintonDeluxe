package de.redlion.badminton.opponent;

import com.badlogic.gdx.math.Vector3;

public class NetworkOpponent extends Opponent {

	public NetworkOpponent(SIDE side,boolean service) {
		super(side, service);
	}

	public void update(Vector3 playerposition) {
		super.update();
	}

}
