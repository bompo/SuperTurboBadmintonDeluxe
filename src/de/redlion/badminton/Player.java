package de.redlion.badminton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Player {

	public enum STATE {
		IDLE, UP, DOWN, LEFT, RIGHT, DOWNLEFT, UPLEFT, DOWNRIGHT, UPRIGHT, AIMING;
	}
	
	public enum AIMING {
		IDLE, UP, DOWN, LEFT, RIGHT, DOWNLEFT, UPLEFT, DOWNRIGHT, UPRIGHT;
	}

	public Vector3 direction = new Vector3(0, 0, 0);
	public Vector3 lastDirection = new Vector3(0, 0, 0);
	public Vector3 position = new Vector3(-2, 7, -0.5f);
	public Vector3 velocity = new Vector3(0,0,0);
	public STATE state = STATE.IDLE;
	public AIMING aiming = AIMING.IDLE;
	public boolean jump = false;
	public float aimTime = 1;
	public float diagonalTime = 0; //currently only used for sliding diagonally  when player didn't release both keys at the same time
	public float moveTime = 0.0f;
	public float momentum =0.97f;

	public Player() {
	}

	public void update() {		
		if(state != STATE.AIMING) {
			
			if (state == STATE.LEFT) {
				position.x = position.x - Gdx.graphics.getDeltaTime() * 4 + velocity.x;
				position.y += velocity.y;
				if(diagonalTime > 1.0f && (direction.idt(new Vector3(-1,1,0)) || direction.idt(new Vector3(-1,-1,0)))) {
					direction = new Vector3(-1, 0, 0);
					diagonalTime = -1;
				}
				else if (!direction.idt(new Vector3(-1,1,0)) && !direction.idt(new Vector3(-1,-1,0)))
					direction = new Vector3(-1, 0, 0);
			}
			if (state == STATE.RIGHT) {
				position.x = position.x + Gdx.graphics.getDeltaTime() * 4 + velocity.x;
				position.y += velocity.y;
				if(diagonalTime > 1.0f && (direction.idt(new Vector3(1,1,0)) || direction.idt(new Vector3(1,-1,0)))) {
					direction = new Vector3(1, 0, 0);
					diagonalTime = -1;
				}
				else if(!direction.idt(new Vector3(1,1,0)) && !direction.idt(new Vector3(1,-1,0)))
					direction = new Vector3(1, 0, 0);
			}
			if (state == STATE.UP) {
				position.x += velocity.x;
				position.y = position.y - Gdx.graphics.getDeltaTime() * 4 + velocity.y;
				if(diagonalTime > 1.0f && (direction.idt(new Vector3(-1,-1,0)) || direction.idt(new Vector3(1,-1,0)))) {
					direction = new Vector3(0, -1, 0);
					diagonalTime = -1;
				}
				else if(!direction.idt(new Vector3(-1,-1,0)) && !direction.idt(new Vector3(1,-1,0))) {
					direction = new Vector3(0, -1, 0);
				}
			}
			if (state == STATE.DOWN) {
				position.x += velocity.x;
				position.y = position.y + Gdx.graphics.getDeltaTime() * 4 + velocity.y;
				if(diagonalTime > 1.0f && (direction.idt(new Vector3(-1,1,0)) || direction.idt(new Vector3(1,1,0)))) {
					direction = new Vector3(0, 1, 0);
					diagonalTime = -1;
				}
				else if(!direction.idt(new Vector3(-1,1,0)) && !direction.idt(new Vector3(1,1,0)))
					direction = new Vector3(0, 1, 0);
			}
			if (state == STATE.DOWNLEFT) {
				position.x = position.x - Gdx.graphics.getDeltaTime() * 3.5f + velocity.x;
				position.y = position.y + Gdx.graphics.getDeltaTime() * 3.5f + velocity.y;
				direction = new Vector3(-1, 1, 0);
				diagonalTime = 0;
			}
			if (state == STATE.UPLEFT) {
				position.x = position.x - Gdx.graphics.getDeltaTime() * 3.5f + velocity.x;
				position.y = position.y - Gdx.graphics.getDeltaTime() * 3.5f + velocity.y;
				direction = new Vector3(-1, -1, 0);
				diagonalTime = 0;
			}
			if (state == STATE.DOWNRIGHT) {
				position.x = position.x + Gdx.graphics.getDeltaTime() * 3.5f + velocity.x;
				position.y = position.y + Gdx.graphics.getDeltaTime() * 3.5f + velocity.y;
				direction = new Vector3(1, 1, 0);
				diagonalTime = 0;
			}
			if (state == STATE.UPRIGHT) {
				position.x = position.x + Gdx.graphics.getDeltaTime() * 3.5f + velocity.x;
				position.y = position.y - Gdx.graphics.getDeltaTime() * 3.5f + velocity.y;
				direction = new Vector3(1, -1, 0);
				diagonalTime = 0;
			}
			if (state == STATE.IDLE) {
				aimTime=1;
				
				velocity.mul(momentum);
				position.x += velocity.x;
				position.y += velocity.y;
				
				if(diagonalTime > 0.0f)
					diagonalTime -= Gdx.graphics.getDeltaTime() / 2;
				else
					diagonalTime = 0;
				
				if(Math.abs(velocity.x) < 0.01f && Math.abs(velocity.y) < 0.01f) {
					direction = new Vector3(0, 0, 0);
					velocity = new Vector3(0, 0, 0);
					moveTime = 0.0f;
				}
			}
			else {
				moveTime += Gdx.graphics.getDeltaTime();
				velocity.add(direction.cpy().mul(Gdx.graphics.getDeltaTime()  * moveTime * 0.5f));
				if(direction.x == 0 && state != STATE.DOWNLEFT && state != STATE.DOWNRIGHT && state != STATE.UPRIGHT && state != STATE.UPLEFT)
					velocity.x *= 0.8f;
				if(direction.y == 0 && state != STATE.DOWNLEFT && state != STATE.DOWNRIGHT && state != STATE.UPRIGHT && state != STATE.UPLEFT)
					velocity.y *= 0.8f;
				if(Math.abs(velocity.x) > 0.1f)
					velocity.x = Math.signum(velocity.x) * 0.1f;
				if(Math.abs(velocity.y) > 0.15f)
					velocity.y = Math.signum(velocity.y) * 0.15f;
			}
			if(diagonalTime > 1.0f)
				diagonalTime = -1;
			if(moveTime > 0.5f)
				moveTime = 0.5f;
			
		}
		if(state == STATE.AIMING) {
			aimTime += Gdx.graphics.getDeltaTime() / 5;
			
			if(diagonalTime > 0.0f)
				diagonalTime -= Gdx.graphics.getDeltaTime();
			else
				diagonalTime = 0;
			
			if (aiming == AIMING.LEFT) {
				position.x = position.x - Gdx.graphics.getDeltaTime() * 0.5f;
			}
			if (aiming == AIMING.RIGHT) {
				position.x = position.x + Gdx.graphics.getDeltaTime() * 0.5f;
			}
			if (aiming == AIMING.UP) {
				position.y = position.y - Gdx.graphics.getDeltaTime() * 0.5f;
			}
			if (aiming == AIMING.DOWN) {
				position.y = position.y + Gdx.graphics.getDeltaTime() * 0.5f;
			}
			if (aiming == AIMING.DOWNLEFT) {
				position.x = position.x - Gdx.graphics.getDeltaTime() * 0.4375f;
				position.y = position.y + Gdx.graphics.getDeltaTime() * 0.4375f;
			}
			if (aiming == AIMING.UPLEFT) {
				position.x = position.x - Gdx.graphics.getDeltaTime() * 0.4375f;
				position.y = position.y - Gdx.graphics.getDeltaTime() * 0.4375f;
			}
			if (aiming == AIMING.DOWNRIGHT) {
				position.x = position.x + Gdx.graphics.getDeltaTime() * 0.4375f;
				position.y = position.y + Gdx.graphics.getDeltaTime() * 0.4375f;
			}
			if (aiming == AIMING.UPRIGHT) {
				position.x = position.x + Gdx.graphics.getDeltaTime() * 0.4375f;
				position.y = position.y - Gdx.graphics.getDeltaTime() * 0.4375f;
			}
			
		}
		else if(state != STATE.IDLE && state != STATE.DOWNLEFT && state != STATE.UPRIGHT && state != STATE.UPLEFT && state != STATE.DOWNRIGHT) {
			if(diagonalTime != -1 )
				diagonalTime += Gdx.graphics.getDeltaTime() * 14;
		}
		
		
		
//		if(state == STATE.IDLE) {
//
//			position.add(velocity.mul(Gdx.graphics.getDeltaTime() * (moveTime * 0.96f)));
//
//		}
//		else {
//			position.add(velocity.mul(moveTime * Gdx.graphics.getDeltaTime() * 3));
//			
//		}
		
		Gdx.app.log("", velocity + "");
		
		if(jump){
			velocity.mul( (float) Math.pow(0.97f, Gdx.graphics.getDeltaTime() * 35.f));
			position.add(velocity.x * Gdx.graphics.getDeltaTime(), velocity.y * Gdx.graphics.getDeltaTime(), velocity.z * Gdx.graphics.getDeltaTime());
			if(position.z <= -0.5f) {
				position.z = position.z + (Gdx.graphics.getDeltaTime() * 3.3f);
			}
			else if(position.z > -0.5f) {
				jump = false;
			}
			
		}

	}
	
	public void jump() {
		jump=true;
		velocity = direction;
		velocity.x *= 6.0f/5;
		velocity.y *= 6.0f/2;
		velocity.z = -6.0f;
	}
	
	public String toString() {
		return "Player State: " + state + " Aiming: " + aiming + " Position: " + position + " Direction: " + direction;
	}

}
