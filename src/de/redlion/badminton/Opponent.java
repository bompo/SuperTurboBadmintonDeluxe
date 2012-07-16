package de.redlion.badminton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Opponent {

	public enum STATE {
		IDLE, UP, DOWN, LEFT, RIGHT, DOWNLEFT, UPLEFT, DOWNRIGHT, UPRIGHT;
	}

	public Vector3 direction = new Vector3(0, 1, 0);
	public Vector3 center = new Vector3(-0.10882504f,-2.8946533f,-0.5f);
	public Vector3 position = center.cpy();
	public Vector3 velocity = new Vector3(0,0,0); //for jumps
	public STATE state = STATE.IDLE;
	public boolean jump = false;

	public Opponent() {
	}

	public void update(Vector3 playerposition) {
		
		Vector3 distance = position.cpy().sub(Resources.getInstance().birdie.currentPosition.cpy());
		Vector3 tmp = playerposition.cpy();
		tmp.x = - playerposition.x;
		tmp.y = 10 - playerposition.y;
		direction = position.cpy().sub(tmp).mul(-1);
		direction.nor();
		
		if(Resources.getInstance().birdie.state == Birdie.STATE.HIT || Resources.getInstance().birdie.currentPosition.cpy().y < 0) {
			if(position.y < 0) {
				position.x = position.x - distance.x * Gdx.graphics.getDeltaTime() / 2;
				position.y = position.y - distance.y * Gdx.graphics.getDeltaTime() / 2;
				position.z = position.z - distance.z * Gdx.graphics.getDeltaTime() / 2;
			}
			else
				position.y = -0.01f;
		}
		else if(Resources.getInstance().birdie.state == Birdie.STATE.HITBYOPPONENT || Resources.getInstance().birdie.state == Birdie.STATE.HELD){
			distance = position.cpy().sub(center.cpy());
			
			position.x = position.x - distance.x * Gdx.graphics.getDeltaTime() / 1;
			position.y = position.y - distance.y * Gdx.graphics.getDeltaTime() / 1;
			position.z = position.z - distance.z * Gdx.graphics.getDeltaTime() / 1;
			
		}
		else if(Resources.getInstance().birdie.state == Birdie.STATE.NONHIT){
			//SCOOOOOREEEE!!
		}
			

	}
	
	public void jump()
	{
//		jump=true;
//		velocity = direction;
//		velocity.x *= 6.0f/5;
//		velocity.y *= 6.0f/2;
//		velocity.z = -6.0f;
	}

}
