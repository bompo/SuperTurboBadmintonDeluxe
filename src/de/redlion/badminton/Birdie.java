package de.redlion.badminton;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import de.redlion.badminton.Player.AIMING;

public class Birdie {

	public enum STATE {
		HIT, NONHIT, HITBYOPPONENT, HELD, PREPARED;
	}

	public Vector3 direction = new Vector3(0, 0, -1);
	public Vector3 currentPosition = new Vector3(-2, 7, -0.5f);
	public Vector3 fromPosition = new Vector3(-2, 7, -0.5f);
	public Vector3 via = new Vector3(0,0,-3);
	public Vector3 toPosition = new Vector3(0, -6, -0.5f);
	public Vector3 velocity = new Vector3(0, 0, 0);

	float angle = 45.0f;
	float acceleration = 6.0f;
	float distance = 2.0f;
	
	final float GRAVITY = 9.81f;

	float t = 0;
	public STATE state = STATE.HELD;

	public Birdie() {
	}

	public void update(Player player) {
		if (state == STATE.HELD || state == STATE.PREPARED) {
			currentPosition = Resources.getInstance().player.position.cpy().add(-0.5f,
					0, 0);
		} else {
			velocity.mul((float) Math.pow(0.97f, Gdx.graphics.getDeltaTime() * 30.f));			
			
			//TODO: http://www.real-world-physics-problems.com/physics-of-volleyball.html			
//			currentPosition.x = fromPosition.x * velocity.x  + (1 - toPosition.x);
//			currentPosition.y = fromPosition.y * velocity.y  + (1 - toPosition.y);
//			currentPosition.z = fromPosition.z * velocity.z  + (1 - toPosition.z);
			
			
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
		
		velocity = new Vector3(0.4f, 0.4f, 0.4f);

		fromPosition = currentPosition.cpy();
		
		if(player.aiming == Player.AIMING.LEFT) {
			toPosition.x = -3;
			via.x = toPosition.x + fromPosition.x;
			via.x/=2;
		}
		else if(player.aiming == Player.AIMING.RIGHT) {
			toPosition.x = 3;
			via.x = fromPosition.x + toPosition.x;
			via.x/=2;
		}
		else {
			toPosition.x = fromPosition.x;
			via.x = fromPosition.x;
		}
		
		
		Gdx.app.log("", via.x + "");
		
	}

	public String toString() {
		return "Birdie State: " + state + " Position: " + currentPosition
				+ " Direction: " + direction;
	}

}
