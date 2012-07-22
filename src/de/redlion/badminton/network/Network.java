package de.redlion.badminton.network;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.net.MalformedURLException;
import java.text.CollationKey;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import de.redlion.badminton.GameSession;
import de.redlion.badminton.Player;
import de.redlion.badminton.opponent.NetworkOpponent;

public class Network {

	private SocketIO socket;
	private Json json;

	public Array<String> messageList = new Array<String>();
	public Set<Room> rooms;
	
	public Array<UpdatePackage> networkUpdates = new Array<UpdatePackage>();

	public boolean connected = false;
	public float timeToConnect = 5;
	
	public boolean startGame = false;

	// network vars
	public String id;
	public HashMap<String, Integer> connectedIDs = new HashMap<String, Integer>();
	public NetworkOpponent opponent;
	
	Player.STATE currentState = Player.STATE.IDLE; 

	static Network instance;

	private Network() {
		json = new Json();
		rooms = new TreeSet<Room>();
		Collections.synchronizedSet(rooms);
		connectToServer();
	}

	private void connectToServer() {
		
		try {
//			socket = new SocketIO("http://localhost:19834");
			socket = new SocketIO("http://superturbobadminton.nodester.com:80");

			socket.connect(new IOCallback() {
				
		        @Override
		        public void onMessage(JSONObject json, IOAcknowledge ack) {
		            try {
		                System.out.println("Server said:" + json.toString(2));
		            } catch (JSONException e) {
		                e.printStackTrace();
		            }
		        }

		        @Override
		        public void onMessage(String data, IOAcknowledge ack) {
		        	json.prettyPrint(data);
		        	String test = new String();
		        	json.readField(test, "player", data);
		        	System.out.println(test);
		            System.out.println("Server said: " + data);
		        }

		        @Override
		        public void onError(SocketIOException socketIOException) {
		            System.out.println("an Error occured");
		            socketIOException.printStackTrace();
		        }

		        @Override
		        public void onDisconnect() {
		        	addMessage("connection terminated");
		            System.out.println("connection terminated.");
		            startGame = false;
		        }

		        @Override
		        public void onConnect() {
		        	connected = true;
		        	addMessage("connected");
		            System.out.println("Connection established");
		        }

		        @Override
		        public void on(String event, IOAcknowledge ack, Object... data) {
		        	System.out.println("Server triggered event '" + event + "'");
		        			        	
		            try {
			        	JSONObject obj  = (JSONObject) data[0];
			        	
		                if (event.equals("init")) {
		                	id = obj.getString("player");
		                	addMessage("player id " + id);
		                	System.out.println("player id " + id);
		                }
		                if (event.equals("roomconnect")) {
		                	rooms.add(new Room(obj.getString("room"),obj.getString("name"),obj.getBoolean("hasPass"),obj.getInt("playerCnt")));
		                	System.out.println("Room " + obj.getString("room") + " added");
				        	addMessage("Room " + obj.getString("room") + " added");
		                }
		                
		                if (event.equals("startgame")) {
		                	System.out.println("startgame");
				        	addMessage("startgame");
				        	startGame = true;
		                }
		                
		                if (event.equals("roomdisconnect")) {
		                	String id = obj.getString("room");
		                	Room roomToRemove = null;
		                	for(Room room:rooms) {
		                		if(room.id.equals(id)) {
		                			roomToRemove = room;
		                			break;
		                		}
		                	}
		                	if(roomToRemove!=null) {
		                		rooms.remove(roomToRemove);
			                	System.out.println("Room " + obj.getString("room") + " removed");
					        	addMessage("Room " + obj.getString("room") + " removed");
		                	}
		                }
		                
		                if (event.equals("playerconnect")) {
		                	connectedIDs.put(obj.getString("player"), obj.getInt("count"));
		                	System.out.println("Player " + obj.getString("player") + ", " + obj.getInt("count") + " connected");

				        	addMessage("player " + obj.getString("player") + ", " + obj.getInt("count") + " connected");
				        	if(connectedIDs.keySet().size() == 1) {
				        		System.out.println("reinit");
				        	}
		                }
		                if (event.equals("ready")) {
		                	System.out.println("Player " + obj.getString("player") + " ready");

				        	addMessage("player " + obj.getString("player") + " ready");
		                }
		                if (event.equals("notready")) {
		                	System.out.println("Player " + obj.getString("player") + " not ready");

				        	addMessage("player " + obj.getString("player") + " not ready");
		                }
		                if (event.equals("death")) {
		                	System.out.println("Player " + obj.getString("player") + " death");

				        	addMessage("player " + obj.getString("player") + " death");
		                }
		                if (event.equals("playerdisconnect")) {
		                	connectedIDs.remove(obj.getString("player"));
		                	connectedIDs.clear();
		                	System.out.println("Player " + obj.getString("player") + " disconnected");
		                	addMessage("player " + obj.getString("player") + " disconnected");
		                }
		                
		                if (event.equals("update")) {
		             
	                			System.out.println("update opponent");	
	                			networkUpdates.add(new UpdatePackage(new Vector3((float) obj.getJSONObject("message").getDouble("positionx"),(float) obj.getJSONObject("message").getDouble("positiony"),0)));
	                			
	                			if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("IDLE")) {
	                				GameSession.getInstance().opponent.state = Player.STATE.IDLE;
	                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("UP")) {
	                				GameSession.getInstance().opponent.state = Player.STATE.UP;
	                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("DOWN")) {
	                				GameSession.getInstance().opponent.state = Player.STATE.DOWN;
	                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("LEFT")) {
	                				GameSession.getInstance().opponent.state = Player.STATE.LEFT;
	                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("RIGHT")) {
	                				GameSession.getInstance().opponent.state = Player.STATE.RIGHT;
	                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("UPLEFT")) {
	                				GameSession.getInstance().opponent.state = Player.STATE.UPLEFT;
	                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("UPRIGHT")) {
	                				GameSession.getInstance().opponent.state = Player.STATE.UPRIGHT;
	                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("DOWNLEFT")) {
	                				GameSession.getInstance().opponent.state = Player.STATE.DOWNLEFT;
	                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("DOWNRIGHT")) {
	                				GameSession.getInstance().opponent.state = Player.STATE.DOWNRIGHT;
	                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("AIMING")) {
	                				GameSession.getInstance().opponent.state = Player.STATE.AIMING;
	                			}
		                	
		                }
		                if (event.equals("synchronize")) {
		                	System.out.println("synchronize opponent");
		                			Vector3 networkPos = new Vector3((float) obj.getJSONObject("message").getDouble("positionx"),(float) obj.getJSONObject("message").getDouble("positiony"),0);
		                			if(networkPos.dst(GameSession.getInstance().opponent.position)>1) {		                				
		                				networkUpdates.add(new UpdatePackage(networkPos));
		                			} else {
			                			networkPos.sub(GameSession.getInstance().opponent.position);
			                			Vector3 newPos = GameSession.getInstance().opponent.position.tmp().add(networkPos.mul(0.1f));
			                			networkUpdates.add(new UpdatePackage(newPos));
		                			}	
		                			
		                			if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("IDLE")) {
		                				GameSession.getInstance().opponent.state = Player.STATE.IDLE;
		                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("UP")) {
		                				GameSession.getInstance().opponent.state = Player.STATE.UP;
		                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("DOWN")) {
		                				GameSession.getInstance().opponent.state = Player.STATE.DOWN;
		                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("LEFT")) {
		                				GameSession.getInstance().opponent.state = Player.STATE.LEFT;
		                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("RIGHT")) {
		                				GameSession.getInstance().opponent.state = Player.STATE.RIGHT;
		                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("UPLEFT")) {
		                				GameSession.getInstance().opponent.state = Player.STATE.UPLEFT;
		                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("UPRIGHT")) {
		                				GameSession.getInstance().opponent.state = Player.STATE.UPRIGHT;
		                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("DOWNLEFT")) {
		                				GameSession.getInstance().opponent.state = Player.STATE.DOWNLEFT;
		                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("DOWNRIGHT")) {
		                				GameSession.getInstance().opponent.state = Player.STATE.DOWNRIGHT;
		                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("AIMING")) {
		                				GameSession.getInstance().opponent.state = Player.STATE.AIMING;
		                			}
		                		
		                }
		                
		                if (event.equals("startround")) {
		                	System.out.println("startround");
		                }
		            } catch (Exception ex) {
		                ex.printStackTrace();
		            }
		        	

		        }
		    });

		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	}
	
	public void update() {
		sendCurrentState(GameSession.getInstance().player);
		for(UpdatePackage update:networkUpdates) {
			GameSession.getInstance().opponent.position.set(update.position.x, update.position.y, update.position.z);
		}
		networkUpdates.clear();
	}

	public void sendMessage(String message) {
		// This line is cached until the connection is established.
		socket.send(message);
	}
	
	public void sendCreateRoom(String roomName) {
		System.out.println("create room " + roomName);

		JSONObject json = new JSONObject();
		try {
			json.putOpt("player", id);
			json.putOpt("roomname", roomName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		socket.emit("createroom", json);
	}
	
	public void sendCreatePrivateRoom(String roomName, String password) {
		System.out.println("create private room " + roomName + " (" + password + ")");

		JSONObject json = new JSONObject();
		try {
			json.putOpt("player", id);
			json.putOpt("roomname", roomName);
			json.putOpt("password", password);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		socket.emit("createprivateroom", json);
	}
	
	public void sendJoinRoom(String roomId) {
		System.out.println("join room " + roomId);

		JSONObject json = new JSONObject();
		try {
			json.putOpt("player", id);
			json.putOpt("roomId", roomId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		socket.emit("joinroom", json);
	}
	
	public void sendLeaveRoom() {
		System.out.println("leave current room ");

		JSONObject json = new JSONObject();
		try {
			json.putOpt("player", id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		socket.emit("leaveroom", json);
	}

	public void sendCurrentState(Player player) {
		if (currentState == player.state)
			return;
		System.out.println("send update");

		JSONObject json = new JSONObject();
		try {
			json.putOpt("state", player.state);
			json.putOpt("positionx", GameSession.getInstance().player.position.x);
			json.putOpt("positiony", -GameSession.getInstance().player.position.y);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		socket.emit("update", json);
		currentState = player.state;
	}

	public void sendSyncState(Player player) {
		System.out.println("send sync");

		JSONObject json = new JSONObject();
		try {
			json.putOpt("state", player.state);
			json.putOpt("positionx", GameSession.getInstance().player.position.x);
			json.putOpt("positiony", GameSession.getInstance().player.position.y);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		socket.emit("synchronize", json);
		currentState = player.state;
	}

	public void sendReady(Player player) {
		System.out.println("send ready");

		JSONObject json = new JSONObject();
		try {
			json.putOpt("player", id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		socket.emit("ready", json);
	}

	public void sendNotReady(Player player) {
		System.out.println("send not ready");

		JSONObject json = new JSONObject();
		try {
			json.putOpt("player", id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		socket.emit("notready", json);
	}

	public void sendHit(Player player) {
		System.out.println("send hit");

		JSONObject json = new JSONObject();
		try {
			json.putOpt("state", player.state);
			json.putOpt("positionx", GameSession.getInstance().player.position.x);
			json.putOpt("positiony", GameSession.getInstance().player.position.y);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		socket.emit("hit", json);
	}

	public static Network getInstance() {
		if (instance != null)
			return instance;
		instance = new Network();
		return instance;
	}

	public void addMessage(String m) {
		if (messageList.size > 5) {
			messageList.removeIndex(0);
		}
		messageList.add(m);
	}
	
	public void sortRooms() {
//		ArrayList<Room> temp = new ArrayList<Room>();
//		try {
//		temp.add(rooms2.first());
//		rooms2.remove(rooms2.first());
//		for(Room r : temp) {
//			
//			if(r.compareTo(temp.get(0)) < 0){
//				if(!r.equals(temp.get(0)))
//					temp.add(0, r);
//			}
//			if(r.compareTo(temp.get(0)) >= 0){
//				if(!r.equals(temp.get(0)))
//					temp.add(r);
//			}
//			
//		}
//		} catch(Exception e) {
//			java.util.Collections.so
//		}
//		
//		sortRooms(rooms2);
//		
//		rooms.clear();
//		rooms.addAll(temp);
		
		ArrayList<Room> chambers = new ArrayList<Room>();
		
		for(Room r : rooms) {
			chambers.add(r);
		}
		
		java.util.Collections.sort(chambers);
		
		
		
		rooms.clear();
		
		
		for(Room ro : chambers) {
			rooms.add(ro);
		}
	}


}
