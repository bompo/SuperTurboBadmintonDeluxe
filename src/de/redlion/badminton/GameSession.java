package de.redlion.badminton;

import de.redlion.badminton.opponent.AIOpponent;
import de.redlion.badminton.opponent.LocalOpponent;
import de.redlion.badminton.opponent.NetworkOpponent;
import de.redlion.badminton.opponent.Opponent;

public class GameSession {
	
	public Player player = new Player(Player.SIDE.BOTTOM);
	public Opponent opponent = new Opponent(Player.SIDE.TOP);
	public Birdie birdie = new Birdie();
	

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
		opponent = new AIOpponent(Player.SIDE.TOP);
	}
	
	public void newMultiPlayerGame() {
		playerScore = 0;
		opponentScore = 0;
		opponent = new NetworkOpponent(Player.SIDE.TOP);
	}
	
	public void newLocalMultiPlayerGame() {
		playerScore = 0;
		opponentScore = 0;
		opponent = new LocalOpponent(Player.SIDE.TOP);
	}


}
