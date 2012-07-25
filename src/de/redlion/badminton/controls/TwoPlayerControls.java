package de.redlion.badminton.controls;

import com.badlogic.gdx.Gdx;

import de.redlion.badminton.Player;
import de.redlion.badminton.opponent.Opponent;

public class TwoPlayerControls extends GameControls {
	
	public Player playerOne;
	public Opponent playerTwo;
	
	public TwoPlayerControls(Player player, Opponent playerTwo) {
		this.playerOne = player;
		this.playerTwo = playerTwo;
	}
	
	
	@Override
	public boolean keyDown(int keycode) {
		keyDownOptions(keycode);

		keyDownPlayer(playerOne, keycode);
		keyDownPlayer(playerTwo, keycode);

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		keyUpPlayer(playerOne, keycode);
		keyUpPlayer(playerTwo, keycode);

		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		x = (int) (x / (float) Gdx.graphics.getWidth() * 800);
		y = (int) (y / (float) Gdx.graphics.getHeight() * 480);
		return false;
	}

	protected int lastTouchX;
	protected int lastTouchY;

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		x = (int) (x / (float) Gdx.graphics.getWidth() * 800);
		y = (int) (y / (float) Gdx.graphics.getHeight() * 480);
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		x = (int) (x / (float) Gdx.graphics.getWidth() * 800);
		y = (int) (y / (float) Gdx.graphics.getHeight() * 480);
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public boolean touchMoved(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
