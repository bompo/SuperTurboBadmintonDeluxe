package de.redlion.badminton;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import de.redlion.badminton.Player.AIMING;

public class Birdie {

	public enum STATE {
		HIT, NONHIT, HITBYOPPONENT, HELD, PREPARED;
	}

	public Vector3 currentPosition = new Vector3(-2, 7, -0.5f);
	public Vector3 fromPosition = new Vector3(-2, 7, -0.5f);
	public Vector3 via = new Vector3(0,0,-3);
	public Vector3 toPosition = new Vector3(0, -6, -0.5f);

	public float acceleration = 1;
	
	float t = 0;
	public STATE state = STATE.HELD;

	public Birdie() {
	}

	public void update(Player player) {
		if (state == STATE.HELD || state == STATE.PREPARED) {
			currentPosition = Resources.getInstance().player.position.cpy().add(-0.5f,
					0, 0);
		} else {
			
			
			currentPosition =  fromPosition.cpy().mul((float) Math.pow(1-t, 2));
			currentPosition.add(via.cpy().mul(2*t*(1-t)));
			currentPosition.add(toPosition.cpy().mul((float) Math.pow(t, 2)));
			
			t+= Gdx.graphics.getDeltaTime() / 2;
			
			
			if (currentPosition.z < 0) {
				//currentPosition.z = currentPosition.z + (Gdx.graphics.getDeltaTime() * 2.f);
			} else {
				state = STATE.NONHIT;
				score();
				reset();
				player.state = Player.STATE.IDLE;
			}
		}
	}

	public void score() {
		if (currentPosition.y < 0) {
			if (Math.abs(currentPosition.x) < 3) {
				if (Math.abs(currentPosition.y) < 7.5) {
					Resources.getInstance().playerScore++;
				} else {
					Resources.getInstance().opponentScore++;
				}
			} else {
				Resources.getInstance().opponentScore++;
			}
		}

		if (currentPosition.y > 0) {
			if (Math.abs(currentPosition.x) < 3)
				if (Math.abs(currentPosition.y) < 7.5)
					Resources.getInstance().opponentScore++;
				else
					Resources.getInstance().playerScore++;
			else
				Resources.getInstance().playerScore++;
		}
	}

	public void reset() {
		
		if (currentPosition.y < 0) {
			currentPosition = Resources.getInstance().player.position.cpy()
					.add(-0.5f, 0, 0);
			toPosition = new Vector3(0, -6, -0.5f);
		} else {
			currentPosition = Resources.getInstance().opponent.position.cpy()
					.add(-0.5f, 0, 0);
			toPosition = new Vector3(0, -6, -0.5f);
			
		}
		t=0;
		state = STATE.HELD;
	}

	public void hit(Player player, boolean smash) {

		acceleration = player.aimTime;
		t=0;
		fromPosition = currentPosition.cpy();
		
		if(player.aiming == Player.AIMING.LEFT) {
			toPosition.x = fromPosition.x - acceleration;
			if(toPosition.x < -3)
				toPosition.x = -3;
			toPosition.y = -3 + (fromPosition.y /3);
		}
		else if(player.aiming == Player.AIMING.RIGHT) {
			toPosition.x = fromPosition.x + acceleration;
			if(toPosition.x > 3)
				toPosition.x = 3;
			toPosition.y = -3 + (fromPosition.y /7);
		}
		else if(player.aiming == Player.AIMING.UPLEFT) {
			toPosition.x = fromPosition.x - 0.8f * acceleration;
			if(toPosition.x < -3)
				toPosition.x = -3;
			toPosition.y = -3 - acceleration + (fromPosition.y /7);
			if(toPosition.y < -7.5)
				toPosition.y = -7.5f;
		}
		else if(player.aiming == Player.AIMING.UPRIGHT) {
			toPosition.x = fromPosition.x + 0.8f * acceleration;
			if(toPosition.x > 3)
				toPosition.x = 3;
			toPosition.y = -3 - acceleration + (fromPosition.y /7);
			if(toPosition.y < -7.5)
				toPosition.y = -7.5f;
		}
		else { 
			toPosition.x = 0;
			toPosition.y = -2.5f * acceleration + (fromPosition.y /7);
		}
		
		via.x = toPosition.x + fromPosition.x;
		via.x/=2;
		
		
		Gdx.app.log("", "" + acceleration);
		
	}
	
	//placeholder for opponent hit
	public void hit(boolean smash) {
		t=0;
		fromPosition = currentPosition.cpy();
		acceleration = 1;
		toPosition.x = 0;
		toPosition.y = 3.5f;
		via.x = toPosition.x + fromPosition.x;
		via.x/=2;
	}

	public String toString() {
		return "Birdie State: " + state + " Position: " + currentPosition;
	}

}
