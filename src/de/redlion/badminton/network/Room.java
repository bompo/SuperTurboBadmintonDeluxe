package de.redlion.badminton.network;

public class Room implements Comparable<Room>{

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

	@Override
	public int compareTo(Room b) {
		
		if (b.name == null && this.name == null) {
		      return 0;
		    }
		    if (this.name == null) {
		      return 1;
		    }
		    if (b.name == null) {
		      return -1;
		    }
		    return this.name.toLowerCase().compareTo(b.name.toLowerCase());
	}
	
}
