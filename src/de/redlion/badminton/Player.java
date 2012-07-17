package de.redlion.badminton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

public class Player {

	public enum STATE {
		IDLE, UP, DOWN, LEFT, RIGHT, DOWNLEFT, UPLEFT, DOWNRIGHT, UPRIGHT, AIMING;
	}
	
	public enum AIMING {
		IDLE, UP, DOWN, LEFT, RIGHT, DOWNLEFT, UPLEFT, DOWNRIGHT, UPRIGHT;
	}
	
	public enum SIDE {
		TOP, BOTTOM;
	}

	final static float SPEED = 2;
	final static float MOMENTUM =0.85f;
	
	public SIDE side = SIDE.BOTTOM;
	
	public Vector3 direction = new Vector3(0, 0, 0);
	public Vector3 lastDirection = new Vector3(0, 0, 0);
	public Vector3 position = new Vector3(-2, 7, -0.5f);
	public Vector3 velocity = new Vector3(0,0,0);
	public STATE state = STATE.IDLE;
	public AIMING aiming = AIMING.IDLE;
	public boolean jump = false;
	
	//charge time
	public float aimTime = 1;
	public float diagonalTime = 0; //currently only used for sliding diagonally  when player didn't release both keys at the same time
	public float moveTime = 0.0f;

	public Player(SIDE side) {
		this.side = side;
		if(side == SIDE.TOP) {
			position = new Vector3(0,-3,0);
		}
	}

	public void update() {		
		if(state != STATE.AIMING) {
			
			if (state == STATE.LEFT) {
				position.x = position.x - Gdx.graphics.getDeltaTime() * SPEED + velocity.x;
				position.y += velocity.y;
				if(diagonalTime > 1.0f && (direction.idt(new Vector3(-1,1,0)) || direction.idt(new Vector3(-1,-1,0)))) {
					direction = new Vector3(-1, 0, 0);
					diagonalTime = -1;
				}
				else if (!direction.idt(new Vector3(-1,1,0)) && !direction.idt(new Vector3(-1,-1,0)))
					direction = new Vector3(-1, 0, 0);
			}
			if (state == STATE.RIGHT) {
				position.x = position.x + Gdx.graphics.getDeltaTime() * SPEED + velocity.x;
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
				position.y = position.y - Gdx.graphics.getDeltaTime() * SPEED * 2 + velocity.y;
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
				position.y = position.y + Gdx.graphics.getDeltaTime() * SPEED * 2 + velocity.y;
				if(diagonalTime > 1.0f && (direction.idt(new Vector3(-1,1,0)) || direction.idt(new Vector3(1,1,0)))) {
					direction = new Vector3(0, 1, 0);
					diagonalTime = -1;
				}
				else if(!direction.idt(new Vector3(-1,1,0)) && !direction.idt(new Vector3(1,1,0)))
					direction = new Vector3(0, 1, 0);
			}
			if (state == STATE.DOWNLEFT) {
				position.x = position.x - Gdx.graphics.getDeltaTime() * SPEED + velocity.x;
				position.y = position.y + Gdx.graphics.getDeltaTime() * SPEED*2 + velocity.y;
				direction = new Vector3(-1, 1, 0);
				diagonalTime = 0;
			}
			if (state == STATE.UPLEFT) {
				position.x = position.x - Gdx.graphics.getDeltaTime() * SPEED + velocity.x;
				position.y = position.y - Gdx.graphics.getDeltaTime() * SPEED*2 + velocity.y;
				direction = new Vector3(-1, -1, 0);
				diagonalTime = 0;
			}
			if (state == STATE.DOWNRIGHT) {
				position.x = position.x + Gdx.graphics.getDeltaTime() * SPEED + velocity.x;
				position.y = position.y + Gdx.graphics.getDeltaTime() * SPEED*2 + velocity.y;
				direction = new Vector3(1, 1, 0);
				diagonalTime = 0;
			}
			if (state == STATE.UPRIGHT) {
				position.x = position.x + Gdx.graphics.getDeltaTime() * SPEED + velocity.x;
				position.y = position.y - Gdx.graphics.getDeltaTime() * SPEED*2 + velocity.y;
				direction = new Vector3(1, -1, 0);
				diagonalTime = 0;
			}
			if (state == STATE.IDLE) {
				aimTime=1;
				
				velocity.mul(MOMENTUM);
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
				if(Math.abs(velocity.x) > 0.05f)
					velocity.x = Math.signum(velocity.x) * 0.05f;
				if(Math.abs(velocity.y) > 0.1f)
					velocity.y = Math.signum(velocity.y) * 0.1f;
			}
			if(diagonalTime > 1.0f)
				diagonalTime = -1;
			if(moveTime > 0.5f)
				moveTime = 0.5f;
			
		}
		if(state == STATE.AIMING) {
			aimTime += Gdx.graphics.getDeltaTime() / 3;
			
			if(aiming != AIMING.IDLE)
				velocity.mul(0.8f);
			
			if(diagonalTime > 0.0f)
				diagonalTime -= Gdx.graphics.getDeltaTime();
			else
				diagonalTime = 0;
			
			if (aiming == AIMING.LEFT) {
				direction = new Vector3(-1, 0, 0);
				position.x = position.x - Gdx.graphics.getDeltaTime() * 0.5f + velocity.x;
				position.y += velocity.y;
			}
			if (aiming == AIMING.RIGHT) {
				direction = new Vector3(1, 0, 0);
				position.x = position.x + Gdx.graphics.getDeltaTime() * 0.5f + velocity.x;
				position.y += velocity.y;
			}
			if (aiming == AIMING.UP) {
				direction = new Vector3(0, -1, 0);
				position.y += velocity.x;
				position.y = position.y - Gdx.graphics.getDeltaTime() * 0.5f + velocity.y;
			}
			if (aiming == AIMING.DOWN) {
				direction = new Vector3(0, 1, 0);
				position.y += velocity.x;
				position.y = position.y + Gdx.graphics.getDeltaTime() * 0.5f + velocity.y;
			}
			if (aiming == AIMING.DOWNLEFT) {
				direction = new Vector3(-1, 1, 0);
				position.x = position.x - Gdx.graphics.getDeltaTime() * 0.4375f + velocity.x;
				position.y = position.y + Gdx.graphics.getDeltaTime() * 0.4375f + velocity.y;
			}
			if (aiming == AIMING.UPLEFT) {
				direction = new Vector3(-1, -1, 0);
				position.x = position.x - Gdx.graphics.getDeltaTime() * 0.4375f + velocity.x;
				position.y = position.y - Gdx.graphics.getDeltaTime() * 0.4375f + velocity.y;
			}
			if (aiming == AIMING.DOWNRIGHT) {
				direction = new Vector3(1, 1, 0);
				position.x = position.x + Gdx.graphics.getDeltaTime() * 0.4375f + velocity.x;
				position.y = position.y + Gdx.graphics.getDeltaTime() * 0.4375f + velocity.y;
			}
			if (aiming == AIMING.UPRIGHT) {
				direction = new Vector3(1, -1, 0);
				position.x = position.x + Gdx.graphics.getDeltaTime() * 0.4375f + velocity.x;
				position.y = position.y - Gdx.graphics.getDeltaTime() * 0.4375f + velocity.y;
			}
			
			if(aiming == AIMING.IDLE) {
				
				velocity.mul(MOMENTUM);
				position.x += velocity.x;
				position.y += velocity.y;
				
				if(Math.abs(velocity.x) < 0.01f && Math.abs(velocity.y) < 0.01f) {
					direction = new Vector3(0, 0, 0);
					velocity = new Vector3(0, 0, 0);
					moveTime = 0.0f;
				}
			}
			
			if(Math.abs(velocity.x) < 0.01f && Math.abs(velocity.y) < 0.01f) {
				direction = new Vector3(0, 0, 0);
				velocity = new Vector3(0, 0, 0);
				moveTime = 0.0f;
			}	
			
		}
		else if(state != STATE.IDLE && state != STATE.DOWNLEFT && state != STATE.UPRIGHT && state != STATE.UPLEFT && state != STATE.DOWNRIGHT) {
			if(diagonalTime != -1 )
				diagonalTime += Gdx.graphics.getDeltaTime() * 14;
		}
		
		//Out of bounds?
		if(side == SIDE.BOTTOM) {
			if(position.y < 0.5f) {
				position.y=0.5f;
			}
			
			if(position.y > 9.0f) {
				position.y= 9.0f;
			}
		}		
		if(side == SIDE.TOP) {
			if(position.y > 0.5f) {
				position.y=0.5f;
			}
			
			if(position.y < -9.0f) {
				position.y= -9.0f;
			}
		}
		if(position.x > 3.5f) {
			position.x=3.5f;
		}
		if(position.x < -3.5f) {
			position.x=-3.5f;
		}

	}
	
	public String toString() {
		return "Player State: " + state + " Aiming: " + aiming + " Position: " + position + " Direction: " + direction;
	}

}
