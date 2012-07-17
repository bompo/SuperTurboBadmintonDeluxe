package de.redlion.badminton.network;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.net.MalformedURLException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

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
	public Array<UpdatePackage> networkUpdates = new Array<UpdatePackage>();

	public boolean connected = false;
	public float timeToConnect = 5;

	// network vars
	public Integer place;
	public String id;
	public HashMap<String, Integer> connectedIDs = new HashMap<String, Integer>();
	public NetworkOpponent opponent;
	
	Player.STATE currentState = Player.STATE.IDLE; 

	static Network instance;

	private Network() {
		json = new Json();
		connectToServer();
	}

	private void connectToServer() {
		
		try {
			socket = new SocketIO("http://localhost:17790");
//			socket = new SocketIO("http://superturbobadminton.nodester.com:80");

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
		                	place = obj.getInt("count"); 
		                	addMessage("joined room " + obj.getString("room") + " as player " +  obj.getInt("count"));
		                	System.out.println(obj.getString("player") + ", " + obj.getInt("count"));
		                }
		                if (event.equals("connect")) {
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
		                if (event.equals("disconnect")) {
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

	public void sendMessage(String message) {
		// This line is cached until the connection is established.
		socket.send(message);
	}

	public void sendCurrentState(Player player) {
		if (currentState == player.state)
			return;
		System.out.println("send update");

		JSONObject json = new JSONObject();
		try {
			json.putOpt("state", player.state);
			json.putOpt("positionx", GameSession.getInstance().player.position.x);
			json.putOpt("positiony", GameSession.getInstance().player.position.y);
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


}
