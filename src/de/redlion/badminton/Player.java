package de.redlion.badminton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Player extends Sprite {
	
	public enum STATE {
		IDLE,UP,DOWN,LEFT,RIGHT,DOWNLEFT,UPLEFT,DOWNRIGHT,UPRIGHT;
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
		if (state ==STATE.UP) {
			setY(getY() + Gdx.graphics.getDeltaTime()*200);
		}	
		if (state == STATE.DOWN) {
			setY(getY() - Gdx.graphics.getDeltaTime()*200);
		}
		if (state == STATE.DOWNLEFT) {
			setX(getX() - Gdx.graphics.getDeltaTime()*150);
			setY(getY() - Gdx.graphics.getDeltaTime()*150);
		}
		if (state == STATE.UPLEFT) {
			setX(getX() - Gdx.graphics.getDeltaTime()*150);
			setY(getY() + Gdx.graphics.getDeltaTime()*150);
		}
		if (state == STATE.DOWNRIGHT) {
			setX(getX() + Gdx.graphics.getDeltaTime()*150);
			setY(getY() - Gdx.graphics.getDeltaTime()*150);
		}
		if (state == STATE.UPRIGHT) {
			setX(getX() + Gdx.graphics.getDeltaTime()*150);
			setY(getY() + Gdx.graphics.getDeltaTime()*150);
		}

	}
	
	public void draw(SpriteBatch spriteBatch) {
		
		super.draw(spriteBatch);
	}
	
}
