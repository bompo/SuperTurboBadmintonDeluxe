package de.redlion.badminton;

import com.badlogic.gdx.math.Vector3;

public class Birdie {
	
	public enum STATE {
		HIT, NONHIT;
	}
	
	public Vector3 direction = new Vector3(0,0,-1);
	public Vector3 position = new Vector3(0,0,0);
	public STATE state = STATE.HIT;
	
	public Birdie() {	
	}
	
	public void update() {
	}
	
}
