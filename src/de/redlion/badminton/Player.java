package de.redlion.badminton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Player {

	public enum STATE {
		IDLE, UP, DOWN, LEFT, RIGHT, DOWNLEFT, UPLEFT, DOWNRIGHT, UPRIGHT;
	}

	public Vector3 direction = new Vector3(0, 0, -1);
	public Vector3 position = new Vector3(-2, 7, 0);
	public STATE state = STATE.IDLE;

	public Player() {
	}

	public void update() {
		if (state == STATE.LEFT) {
			position.x = position.x - Gdx.graphics.getDeltaTime() * 4;
			direction = new Vector3(-1, 0, 0);
		}
		if (state == STATE.RIGHT) {
			position.x = position.x + Gdx.graphics.getDeltaTime() * 4;
			direction = new Vector3(1, 0, 0);
		}
		if (state == STATE.UP) {
			position.y = position.y - Gdx.graphics.getDeltaTime() * 4;
			direction = new Vector3(0, -1, 0);
		}
		if (state == STATE.DOWN) {
			position.y = position.y + Gdx.graphics.getDeltaTime() * 4;
			direction = new Vector3(0, 1, 0);
		}
		if (state == STATE.DOWNLEFT) {
			position.x = position.x - Gdx.graphics.getDeltaTime() * 3.5f;
			position.y = position.y + Gdx.graphics.getDeltaTime() * 3.5f;
			direction = new Vector3(-1, 1, 0);
		}
		if (state == STATE.UPLEFT) {
			position.x = position.x - Gdx.graphics.getDeltaTime() * 3.5f;
			position.y = position.y - Gdx.graphics.getDeltaTime() * 3.5f;
			direction = new Vector3(-1, -1, 0);
		}
		if (state == STATE.DOWNRIGHT) {
			position.x = position.x + Gdx.graphics.getDeltaTime() * 3.5f;
			position.y = position.y + Gdx.graphics.getDeltaTime() * 3.5f;
			direction = new Vector3(1, 1, 0);
		}
		if (state == STATE.UPRIGHT) {
			position.x = position.x + Gdx.graphics.getDeltaTime() * 3.5f;
			position.y = position.y - Gdx.graphics.getDeltaTime() * 3.5f;
			direction = new Vector3(1, -1, 0);
		}
		if (state == STATE.IDLE) {
			direction = new Vector3(0, 0, 0);
		}

	}

}
