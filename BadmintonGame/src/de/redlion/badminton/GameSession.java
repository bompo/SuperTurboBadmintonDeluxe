package de.redlion.badminton;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import de.redlion.badminton.opponent.AIOpponent;
import de.redlion.badminton.opponent.LocalOpponent;
import de.redlion.badminton.opponent.NetworkOpponent;
import de.redlion.badminton.opponent.Opponent;

public class GameSession {
	
	public Player player = new Player(Player.SIDE.BOTTOM, true);
	public Opponent opponent = new Opponent(Player.SIDE.TOP, false);
	public Birdie birdie = new Birdie();
	
	public BoundingBox borders = new BoundingBox(new Vector3(-6.5f, 0, -14f), new Vector3(6.5f, 0, 14f));
	

	public int playerScore = 0;
	public int opponentScore = 0;
	
	public static GameSession instance;

	public static GameSession getInstance() {
		if (instance == null) {
			instance = new GameSession();
		}
		return instance;
	}
	
	public void newSinglePlayerGame() {
		playerScore = 0;
		opponentScore = 0;
		opponent = new AIOpponent(Player.SIDE.TOP, false);
	}
	
	public void newMultiPlayerGame() {
		playerScore = 0;
		opponentScore = 0;
		opponent = new NetworkOpponent(Player.SIDE.TOP, false);
	}
	
	public void newLocalMultiPlayerGame() {
		playerScore = 0;
		opponentScore = 0;
		opponent = new LocalOpponent(Player.SIDE.TOP, false);
	}


}
