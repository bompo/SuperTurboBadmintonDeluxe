package de.redlion.badminton;

import com.badlogic.gdx.math.Vector3;

public class Opponent extends Player {

	public Opponent(SIDE side) {
		super(side);
	}

	public void update(Vector3 playerposition) {
		super.update();
		
		//AI		
		if(Resources.getInstance().birdie.state == Birdie.STATE.HIT && Resources.getInstance().birdie.currentPosition.y < 3) {
			direction = position.cpy().sub(Resources.getInstance().birdie.currentPosition.cpy()).mul(-1);
			direction.nor();
		}
		else if(Resources.getInstance().birdie.state == Birdie.STATE.HITBYOPPONENT || Resources.getInstance().birdie.state == Birdie.STATE.HELD){
			direction = position.cpy().sub(new Vector3(0,-3,0)).mul(-1);
			direction.nor();			
		}
		
		if(direction.x>0.5) {
			state = Player.STATE.RIGHT;
			if(direction.y>0.5) {
				state = Player.STATE.DOWNRIGHT;
			} else if (direction.y<-0.5) {
				state = Player.STATE.UPRIGHT;
			}			
		} else if (direction.x<-0.5) {
			state = Player.STATE.LEFT;
			if(direction.y>0.5) {
				state = Player.STATE.DOWNLEFT;
			} else if (direction.y<-0.5) {
				state = Player.STATE.UPLEFT;
			}
		} else {		
			if(direction.y>0.5) {
				state = Player.STATE.DOWN;
			} else if (direction.y<-0.5) {
				state = Player.STATE.UP;
			} else {
				state = Player.STATE.IDLE;
			}
		}
			

	}

}
