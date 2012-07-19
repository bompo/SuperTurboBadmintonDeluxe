package de.redlion.badminton.network;

public class Room {

	public String id;
	public String name;
	public boolean hasPass;
	public int playersCnt;
	
	public Room(String id, String name, boolean hasPass, int playersCnt) {
		this.id = id;
		this.name = name;
		this.hasPass = hasPass;
		this.playersCnt = playersCnt;
	}
	
}
