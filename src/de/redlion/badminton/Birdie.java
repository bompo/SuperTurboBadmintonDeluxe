package de.redlion.badminton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

public class Birdie {
	
	public enum STATE {
		HIT, NONHIT, HITBYOPPONENT, HELD;
	}
	
	public Vector3 direction = new Vector3(0,0,-1);
	public Vector3 position = new Vector3(-2, 7, -0.5f);
	public Vector3 velocity = new Vector3(0,0,0);
	
	float acceleration = 6.0f;
	
	public STATE state = STATE.HELD;
	
	public Birdie() {	
	}
	
	public void update() {
		if(state == STATE.HELD) {
			position = Resources.getInstance().player.position.cpy().add(-0.5f, 0, 0);
		}
		else {
			velocity.mul( (float) Math.pow(0.97f, Gdx.graphics.getDeltaTime() * 30.f));
			position.add(velocity.x * Gdx.graphics.getDeltaTime(), velocity.y * Gdx.graphics.getDeltaTime(), velocity.z * Gdx.graphics.getDeltaTime());
			if(position.z < 0) {
				position.z = position.z + (Gdx.graphics.getDeltaTime() * 2.f);
			}
			else {
				state = STATE.NONHIT;
				if(position.y < 0)
					Resources.getInstance().playerScore++;
				else
					Resources.getInstance().opponentScore++;
				
				reset();
			}
		}
	}
	
	public void reset() {
		if(position.y < 0)
			position = Resources.getInstance().player.position.cpy().add(-0.5f, 0, 0);
		else
			position = Resources.getInstance().opponent.position.cpy().add(-0.5f, 0, 0);
		state = STATE.HELD;
	}
	
	public void hit(Vector3 direction) {
		velocity = direction.cpy();
		velocity.x *= acceleration/2;
		velocity.y *= acceleration;
		velocity.z = -acceleration;
		//velocity.add(direction.x * acceleration * Gdx.graphics.getDeltaTime(), direction.y * acceleration * Gdx.graphics.getDeltaTime(), direction.z * acceleration * Gdx.graphics.getDeltaTime());
	}
	
}
