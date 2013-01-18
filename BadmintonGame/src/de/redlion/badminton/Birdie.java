package de.redlion.badminton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

public class Birdie {

	public enum STATE {
		HIT, NONHIT, HITBYOPPONENT, HELD, PREPARED;
	}

	public Vector3 currentPosition = new Vector3(-2, 0.5f, 7);
	public Vector3 fromPosition = new Vector3(-2, 0.5f, 7);
	public Vector3 via1 = new Vector3(0,7,1);
	public Vector3 via2 = new Vector3(0,7,-1);
	public Vector3 toPosition = new Vector3(0, -1, -6);

	public Vector3 tangent = new Vector3(0,0,1);
	public Vector3 up = new Vector3(0,1,0);
	
	public float maxHeight = 10;
	
	boolean smash = false;
	
	public Array<Vector3> trajectoryPath = new Array<Vector3>();

	public float acceleration = 1;
	public final float speed = 0.75f;
	
	float t = 0;
	public STATE state = STATE.HELD;

	public Birdie() {
	}

	public void update() {
		if (state == STATE.HELD || state == STATE.PREPARED) {
			currentPosition = GameSession.getInstance().player.position.cpy().add(-0.5f, 0, 0);

		} else {
					
			currentPosition = fromPosition.cpy().mul((float) Math.pow(1-t, 3));
			currentPosition.add(via1.cpy().mul(3 * t).mul((float) Math.pow(1-t, 2)));
			currentPosition.add(via2.cpy().mul((float) (3 * Math.pow(t, 2))).mul(1-t));
			currentPosition.add(toPosition.cpy().mul((float) Math.pow(t, 3)));
			
			tangent = via1.cpy().mul((float) (-3 * Math.pow(t, 2)) + 4*t -1);
			tangent.add(fromPosition.cpy().mul((float) Math.pow(t-1, 2)));
			tangent.add((via2.cpy().mul(3*t - 2).sub(toPosition.cpy().mul(t))).mul(t));
			tangent.mul(-3);
			tangent.nor();
			
			up= (fromPosition.cpy().mul(t-1).add(via1.cpy().mul(2-3*t)).add(via2.cpy().mul(3*t)).sub(toPosition.cpy().mul(t)).sub(via2)).mul(-6);
			if(state == Birdie.STATE.HITBYOPPONENT)
				up.mul(-1);
			up.nor();
			
			if(!smash)
				t+= (Gdx.graphics.getDeltaTime()) * acceleration * speed;
			else
				t+= (Gdx.graphics.getDeltaTime()) * acceleration * speed;
			
			if(acceleration > 1.5f)
				trajectoryPath.add(currentPosition);
			else
				trajectoryPath.clear();
			
			if (currentPosition.y >= 0) {
				//currentPosition.z = currentPosition.z + (Gdx.graphics.getDeltaTime() * 2.f);
			} else {
				state = STATE.NONHIT;
				score();
				reset();
			}
						
			//accelerate if falling down, else decrease speed	
			maxHeight = Math.max(maxHeight, -currentPosition.z);

			if(maxHeight == -currentPosition.z) {
				acceleration -= Gdx.graphics.getDeltaTime()/1.8f;
			} else {
				acceleration += Gdx.graphics.getDeltaTime()/1.8f;
			}
			if(acceleration<0.1) {
				acceleration = 0.1f;
			}
			
		}
	}

	public void score() {
		if (currentPosition.z < 0) {
			if (Math.abs(currentPosition.x) < 3) {
				if (Math.abs(currentPosition.z) < 7.5) {
					GameSession.getInstance().playerScore++;
				} else {
					GameSession.getInstance().opponentScore++;
				}
			} else {
				GameSession.getInstance().opponentScore++;
			}
		}

		if (currentPosition.z > 0) {
			if (Math.abs(currentPosition.x) < 3)
				if (Math.abs(currentPosition.z) < 7.5)
					GameSession.getInstance().opponentScore++;
				else
					GameSession.getInstance().playerScore++;
			else
				GameSession.getInstance().playerScore++;
		}
	}

	public void reset() {
		
		if (currentPosition.z < 0) {
			currentPosition = GameSession.getInstance().player.position.cpy()
					.add(-0.5f, 0, 0);
			currentPosition.y = 0f;
			toPosition = new Vector3(0, -6, -0.5f);			
			if(GameSession.getInstance().player.state != Player.STATE.AIMING) {
				GameSession.getInstance().player.switchState();
			}
			
			if(GameSession.getInstance().opponent.state == Player.STATE.AIMING) {
				GameSession.getInstance().opponent.switchState();
			}
			
			GameSession.getInstance().player.service = true;
			GameSession.getInstance().opponent.service = false;
		} else {
			currentPosition = GameSession.getInstance().opponent.position.cpy()
					.add(-0.5f, 0, 0);
			currentPosition.y = 0f;
			toPosition = new Vector3(0, -6, -0.5f);
			if(GameSession.getInstance().opponent.state != Player.STATE.AIMING) {
				GameSession.getInstance().opponent.switchState();	
			}			
			
			if(GameSession.getInstance().player.state == Player.STATE.AIMING) {
				GameSession.getInstance().player.switchState();
			}
			
			GameSession.getInstance().player.service = false;
			GameSession.getInstance().opponent.service = true;
		}
		t=0;
		state = STATE.HELD;

		smash = false;
		
		trajectoryPath.clear();
	}

	public void hit(Player player, boolean high) {
		maxHeight = 0;
		
		//set player states
		player.service = false;
		player.aimTime = 1;
		
		acceleration = player.aimTime;
		if(acceleration > 1.f)
			acceleration = 1.f;
		t=0;
		fromPosition = currentPosition.cpy();
		
		if(player.side == Player.SIDE.BOTTOM) {
				
			if(player.aiming == Player.AIMING.LEFT) {
				toPosition.x = fromPosition.x - acceleration * 4;
				if(toPosition.x < GameSession.getInstance().borders.min.x)
					toPosition.x = GameSession.getInstance().borders.min.x;
				toPosition.z = GameSession.getInstance().borders.min.z + (fromPosition.z /3);
			}
			else if(player.aiming == Player.AIMING.RIGHT) {
				toPosition.x = fromPosition.x + acceleration * 4;
				if(toPosition.x > GameSession.getInstance().borders.max.x)
					toPosition.x = GameSession.getInstance().borders.max.x;
				toPosition.z = GameSession.getInstance().borders.min.z + (fromPosition.z /7);
			}
			else if(player.aiming == Player.AIMING.UPLEFT) {
				toPosition.x = fromPosition.x - 0.8f * acceleration * 4;
				if(toPosition.x < GameSession.getInstance().borders.min.x)
					toPosition.x = GameSession.getInstance().borders.min.x;
				toPosition.z = -3 - acceleration * 4 + (fromPosition.z /7);
				if(toPosition.z < GameSession.getInstance().borders.min.z)
					toPosition.z = GameSession.getInstance().borders.min.z;
			}
			else if(player.aiming == Player.AIMING.UPRIGHT) {
				toPosition.x = fromPosition.x + 0.8f * acceleration * 4;
				if(toPosition.x > GameSession.getInstance().borders.max.x)
					toPosition.x = GameSession.getInstance().borders.max.x;
				toPosition.z = -3 - acceleration * 4 + (fromPosition.z /7);
				if(toPosition.z < GameSession.getInstance().borders.min.z)
					toPosition.z = GameSession.getInstance().borders.min.z;
			}
			else { 
				toPosition.x = 0;
				toPosition.z = -2.5f * acceleration * 4 + (fromPosition.z /7);
			}
				
		} else {
			
			if(player.aiming == Player.AIMING.LEFT) {
				toPosition.x = fromPosition.x + acceleration * 4;
				if(toPosition.x < GameSession.getInstance().borders.max.x)
					toPosition.x = GameSession.getInstance().borders.max.x;
				toPosition.z = GameSession.getInstance().borders.max.z + (fromPosition.z /3);
			}
			else if(player.aiming == Player.AIMING.RIGHT) {
				toPosition.x = fromPosition.x - acceleration * 4;
				if(toPosition.x > GameSession.getInstance().borders.min.x)
					toPosition.x = GameSession.getInstance().borders.min.x;
				toPosition.z = GameSession.getInstance().borders.max.z + (fromPosition.z /7);
			}
			else if(player.aiming == Player.AIMING.UPLEFT) {
				toPosition.x = fromPosition.x + 0.8f * acceleration * 4;
				if(toPosition.x < GameSession.getInstance().borders.min.x)
					toPosition.x = GameSession.getInstance().borders.min.x;
				toPosition.z = 3 + acceleration * 4 + (fromPosition.z /7);
				if(toPosition.z < GameSession.getInstance().borders.max.z)
					toPosition.z = GameSession.getInstance().borders.max.z;
			}
			else if(player.aiming == Player.AIMING.UPRIGHT) {
				toPosition.x = fromPosition.x - 0.8f * acceleration * 4;
				if(toPosition.x > GameSession.getInstance().borders.max.x)
					toPosition.x = GameSession.getInstance().borders.max.x;
				toPosition.z = 3 + acceleration * 4 + (fromPosition.z /7);
				if(toPosition.z < GameSession.getInstance().borders.max.z)
					toPosition.z = GameSession.getInstance().borders.max.z;
			}
			else { 
				toPosition.x = 0;
				toPosition.z = 2.5f * acceleration * 4 + (fromPosition.z /7);
			}
			
			
		}
		
		if(acceleration > 1.5f && !high) {
			toPosition.z -= 1;
			toPosition.z -= 1;
		}
		
		fuzzyPosition(toPosition, 2.f - acceleration);
		
		float middleY = (fromPosition.z + toPosition.z) /1.5f;
		
		via1.z = fromPosition.z + middleY/2;
		via2.z = toPosition.z + middleY/2;
		
		float middleX = (fromPosition.x + toPosition.x) / 2;
		
		via1.x = fromPosition.x * 0.7f + middleX * 0.3f;
		via2.x = toPosition.x * 0.7f + middleX * 0.3f;
		
		if(high) {
			via1.y = 12;
			via2.y = 12;
		}
		else {
			via1.y = 8;
			via2.y = 8;
			
			if(acceleration > 1.5f) {
				via1.y = -2.0f;
				via2.y = -1.5f;
				
				smash = true;
				Gdx.app.log("S M A S H", toPosition + "");
			}
		}

		System.out.println("Hit");
		System.out.println(fromPosition);
		System.out.println(via1);
		System.out.println(via2);
		System.out.println(toPosition);		
	}
		
	public void fuzzyPosition(Vector3 pos, float aimTime) {		
		float x = (float) Math.random() * aimTime * 2 - aimTime;
		float z = (float) Math.random() * aimTime * 2 - aimTime;
		
		pos.x += x;
		pos.z += z;		
	}

	public String toString() {
		return "Birdie State: " + state + " Acceleration: " + (float) MathUtils.round(acceleration * 10) / 10. + " Position: " 
				+ (float) MathUtils.round(currentPosition.x * 10) / 10. + " " 
				+ (float) MathUtils.round(currentPosition.y * 10) / 10. + " "
				+ (float) MathUtils.round(currentPosition.z * 10) / 10.;
	}

}
