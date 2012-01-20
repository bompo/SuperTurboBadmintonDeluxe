package de.redlion.badminton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Player extends Sprite {
	
	public enum STATE {
		IDLE,FORWARD,BACKWARD,LEFT,RIGHT;
	}
	
	public Vector3 direction = new Vector3(0,0,-1);
	public STATE state = STATE.IDLE;
	
	public Player() {
		super();	
		
		this.set(Resources.getInstance().player);
		
		setX(500);
		setY(150);
	}
	
	public void update() {
		if (state == STATE.LEFT) {
			setX(getX() - Gdx.graphics.getDeltaTime()*200);
		}	
		if (state == STATE.RIGHT) {
			setX(getX() + Gdx.graphics.getDeltaTime()*200);
		}
		if (state ==STATE.FORWARD) {
			setY(getY() + Gdx.graphics.getDeltaTime()*200);
		}	
		if (state == STATE.BACKWARD) {
			setY(getY() - Gdx.graphics.getDeltaTime()*200);
		}	
	}
	
	public void draw(SpriteBatch spriteBatch) {
		
		super.draw(spriteBatch);
	}
	
}
