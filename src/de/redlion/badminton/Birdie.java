package de.redlion.badminton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Birdie {

	public enum STATE {
		HIT, NONHIT, HITBYOPPONENT, HELD, PREPARED;
	}

	public Vector3 currentPosition = new Vector3(-2, 7, -0.5f);
	public Vector3 fromPosition = new Vector3(-2, 7, -0.5f);
	public Vector3 via1 = new Vector3(0,1,-3);
	public Vector3 via2 = new Vector3(0,-1,-3);
	public Vector3 toPosition = new Vector3(0, -6, 1.0f);

	public Vector3 tangent = new Vector3(0,0,0);
	public Vector3 up = new Vector3(0,0,0);
	
	public float maxHeight = 0;
	
	boolean smash = false;
	
	public Array<Vector3> trajectoryPath = new Array<Vector3>();

	public float acceleration = 1;
	
	float t = 0;
	public STATE state = STATE.HELD;

	public Birdie() {
	}

	public void update() {
		if (state == STATE.HELD || state == STATE.PREPARED) {
			currentPosition = Resources.getInstance().player.position.cpy().add(-0.5f,
					0, 0);

		} else {
		
			
//			currentPosition =  fromPosition.cpy().mul((float) Math.pow(1-t, 2));
//			currentPosition.add(via.cpy().mul(2*t*(1-t)));
//			currentPosition.add(toPosition.cpy().mul((float) Math.pow(t, 2)));
//			
//			tangent = fromPosition.cpy().sub(via.cpy().mul(2)).add(toPosition.cpy()).mul(2*t);
//			tangent.add(via.cpy().mul(2));
//			tangent.sub(fromPosition.cpy().mul(2));
//			tangent.nor();
//			
//			up= fromPosition.cpy().sub(via.cpy().mul(2)).add(toPosition).mul(2);
//			up.nor();
//			
//			t+= Gdx.graphics.getDeltaTime() / 2;
			
			currentPosition = fromPosition.cpy().mul((float) Math.pow(1-t, 3));
			currentPosition.add(via1.cpy().mul(3*t).mul((float) Math.pow(1-t, 2)));
			currentPosition.add(via2.cpy().mul((float) (3*Math.pow(t, 2))).mul(1-t));
			currentPosition.add(toPosition.cpy().mul((float) Math.pow(t, 3)));
			
			tangent = via1.cpy().mul((float) (-3*Math.pow(t, 2)) + 4*t -1);
			tangent.add(fromPosition.cpy().mul((float) Math.pow(t-1, 2)));
			tangent.add((via2.cpy().mul(3*t - 2).sub(toPosition.cpy().mul(t))).mul(t));
			tangent.mul(-3);
			tangent.nor();
			
			up= (fromPosition.cpy().mul(t-1).add(via1.cpy().mul(2-3*t)).add(via2.cpy().mul(3*t)).sub(toPosition.cpy().mul(t)).sub(via2)).mul(-6);
			if(state == Birdie.STATE.HITBYOPPONENT)
				up.mul(-1);
			up.nor();
			
			if(!smash)
				t+= (Gdx.graphics.getDeltaTime()) * acceleration;
			else
				t+= (Gdx.graphics.getDeltaTime()) *acceleration;
			
			if(acceleration > 1.5f)
				trajectoryPath.add(currentPosition);
			else
				trajectoryPath.clear();
			
			if (currentPosition.z < 0 && t<=1.0f) {
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
			currentPosition.z = -0.5f;
			toPosition = new Vector3(0, -6, -0.5f);
		} else {
			currentPosition = Resources.getInstance().opponent.position.cpy()
					.add(-0.5f, 0, 0);
			currentPosition.z = -0.5f;
			toPosition = new Vector3(0, -6, -0.5f);
			
		}
		t=0;
		state = STATE.HELD;
		Resources.getInstance().player.state = Player.STATE.IDLE;
		
		smash = false;
		
		trajectoryPath.clear();
	}

	public void hit(Player player, boolean high) {
		maxHeight = 0;
		
		acceleration = player.aimTime;
		if(acceleration > 1.f)
			acceleration = 1.f;
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
		
		if(acceleration > 1.5f && !high) {
			toPosition.y -= 1;
			toPosition.y -= 1;
		}
		
		fuzzyPosition(toPosition, 2.f - acceleration);
		
		float middleY = (fromPosition.y + toPosition.y) /1.5f;
		
		via1.y = fromPosition.y + middleY/2;
		via2.y = toPosition.y + middleY/2;
		
		float middleX = (fromPosition.x + toPosition.x) / 2;
		
		via1.x = fromPosition.x * 0.7f + middleX * 0.3f;
		via2.x = toPosition.x * 0.7f + middleX * 0.3f;
		
		if(high) {
			via1.z = -4;
			via2.z = -4;
		}
		else {
			via1.z = -3;
			via2.z = -3;
			
			if(acceleration > 1.5f) {
				via1.z = -2.0f;
				via2.z = -1.5f;
				
				smash = true;
				Gdx.app.log("S M A S H", toPosition + "");
			}
		}
		
	}
	
	//placeholder for opponent hit
	public void hit(boolean smash) {
		maxHeight = 0;
		
		smash = false;
		t=0;
		fromPosition = currentPosition.cpy();
		acceleration = 1;
		toPosition.x = 0;
		toPosition.y = 3.5f;
		
		float middleY = (fromPosition.y + toPosition.y) / 2;
		
		via1.y = fromPosition.y + middleY/2;
		via2.y = toPosition.y + middleY/2;
		
		via1.x = 1/3 * toPosition.x + fromPosition.x;
		
		via2.x = toPosition.x + 1/3 * fromPosition.x;

	}
	
	public void fuzzyPosition(Vector3 pos, float aimTime) {
		
		float x = (float) Math.random() * aimTime * 2 - aimTime;
		float y = (float) Math.random() * aimTime * 2 - aimTime;
		
		pos.x += x;
		pos.y += y;	
		
		
	}

	public String toString() {
		return "Birdie State: " + state + " Acceleration: " + acceleration;
	}

}
