package de.redlion.badminton.network;

public class Room {

	public String id;
	public String name;
	public boolean hasPass;
	
	public Room(String id, String name, boolean hasPass) {
		this.id = id;
		this.name = name;
		this.hasPass = hasPass;
	}
	
}
