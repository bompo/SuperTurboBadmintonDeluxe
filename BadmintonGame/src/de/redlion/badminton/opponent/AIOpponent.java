package de.redlion.badminton.opponent;

import com.badlogic.gdx.math.Vector3;

import de.redlion.badminton.Birdie;
import de.redlion.badminton.GameSession;
import de.redlion.badminton.Player;

public class AIOpponent extends Opponent {

	public AIOpponent(SIDE side,boolean service) {
		super(side, service);
	}

	public void update(Vector3 playerposition) {
		super.update();
		
		//AI		
		if(GameSession.getInstance().birdie.state == Birdie.STATE.HIT && GameSession.getInstance().birdie.currentPosition.y < 10) {
			direction = position.cpy().sub(GameSession.getInstance().birdie.currentPosition.cpy()).mul(-1);
			direction.nor();
		}
		else if(GameSession.getInstance().birdie.state == Birdie.STATE.HITBYOPPONENT || GameSession.getInstance().birdie.state == Birdie.STATE.HELD){
			direction = position.cpy().sub(new Vector3(0,0,-6)).mul(-1);
			direction.nor();			
		}

		if (direction.x > 0.5) {
			state = Player.STATE.RIGHT;
			if (direction.z > 0.5) {
				state = Player.STATE.DOWNRIGHT;
			} else if (direction.z < -0.5) {
				state = Player.STATE.UPRIGHT;
			}
		} else if (direction.z < -0.5) {
			state = Player.STATE.LEFT;
			if (direction.z > 0.5) {
				state = Player.STATE.DOWNLEFT;
			} else if (direction.z < -0.5) {
				state = Player.STATE.UPLEFT;
			}
		} else {
			if (direction.z > 0.5) {
				state = Player.STATE.DOWN;
			} else if (direction.z < -0.5) {
				state = Player.STATE.UP;
			} else {
				state = Player.STATE.IDLE;
			}
		}
			

	}

}
