package de.redlion.badminton;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector3;

import de.redlion.badminton.Player.AIMING;

public class Birdie {

	public enum STATE {
		HIT, NONHIT, HITBYOPPONENT, HELD, PREPARED;
	}

	public Vector3 direction = new Vector3(0, 0, -1);
	public Vector3 currentPosition = new Vector3(-2, 7, -0.5f);
	public Vector3 fromPosition = new Vector3(-2, 7, -0.5f);
	public Vector3 toPosition = new Vector3(-2, 7, -0.5f);
	public Vector3 velocity = new Vector3(0, 0, 0);
	
	CatmullRomSpline spline = new CatmullRomSpline();
	ArrayList<Vector3> path = new ArrayList<Vector3>();

	float acceleration = 6.0f;

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
			
			
			if(path.size() > 0) {
				currentPosition = path.get(0);
				path.remove(0);
			
//			if (currentPosition.z < 0) {
//				currentPosition.z = currentPosition.z + (Gdx.graphics.getDeltaTime() * 2.f);
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
		path = new ArrayList<Vector3>();
		if (currentPosition.y < 0) {
			currentPosition = Resources.getInstance().player.position.cpy()
					.add(-0.5f, 0, 0);
			toPosition = Resources.getInstance().player.position.cpy().add(
					-0.5f, 0, 0);
			fromPosition = Resources.getInstance().player.position.cpy().add(
					-0.5f, 0, 0);
		} else {
			currentPosition = Resources.getInstance().opponent.position.cpy()
					.add(-0.5f, 0, 0);
			toPosition = Resources.getInstance().opponent.position.cpy().add(
					-0.5f, 0, 0);
			fromPosition = Resources.getInstance().opponent.position.cpy().add(
					-0.5f, 0, 0);
		}
		state = STATE.HELD;
	}

	public void hit(Player player, boolean smash) {
		fromPosition.set(currentPosition);
		velocity = new Vector3(2, 0, 2);
		
		if (player.aiming == Player.AIMING.LEFT)
			toPosition.set(2, 2, 0);
		else if (player.aiming == Player.AIMING.RIGHT)
			toPosition.set(-2, 2, 0);
		else
			toPosition.set(-2, 2, 0);
		
		spline.add(fromPosition);
		spline.add(new Vector3(2,0,-2));
		spline.add(toPosition);
		path = (ArrayList<Vector3>) spline.getPath(50);
	}

	public String toString() {
		return "Birdie State: " + state + " Position: " + currentPosition
				+ " Direction: " + direction;
	}

}
