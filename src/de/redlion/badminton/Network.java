package de.redlion.badminton;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.net.MalformedURLException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import de.redlion.badminton.opponent.NetworkOpponent;


public class Network {
	
	private SocketIO socket;
	private Json json;
	
	public Array<String> messageList = new Array<String>();
	
	public boolean connected = false;
	public float timeToConnect = 5;

	//network vars
	public Integer place;
	public String id;
	public HashMap<String,Integer> connectedIDs = new HashMap<String,Integer>();

	public Array<NetworkOpponent> opponents = new Array<NetworkOpponent>();
	
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
				        		if(gameSession!=null) {
					        		gameSession.waitingForOtherPlayers = false;
					        		gameSession.winLoseCounter = 0;
					        		gameSession.initLevel();
				        		}
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
		                	NetworkShip remove = enemies.get(0);
		                	for(NetworkShip enemie:enemies) {
		                		if(enemie.id == obj.getString("player")) {
		                			remove = enemie;
		                		}
		                	}
		                	remove.life = -1;
		                	System.out.println("Player " + obj.getString("player") + " disconnected");
		                	addMessage("player " + obj.getString("player") + " disconnected");
		                	
		                	if(connectedIDs.keySet().size() == 0) {
				        		gameSession.waitingForOtherPlayers = true;
				        		gameSession.winLoseCounter = 0;
				        		gameSession.initLevel();
				        	}
		                }
		                
		                if (event.equals("update")) {
		                	System.out.println("update");
		                	for(NetworkShip ship:enemies) {
		                		if(ship.id.equalsIgnoreCase(obj.getString("player"))) {
		                			System.out.println("update " + ship.id);	
		                			gameSession.networkUpdates.add(new UpdatePackage(ship, new Vector2((float) obj.getJSONObject("message").getDouble("positionx"),(float) obj.getJSONObject("message").getDouble("positiony")), (float)obj.getJSONObject("message").getDouble("angle")));
		                			
		                			if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("IDLE")) {
		                				ship.state = NetworkShip.STATE.IDLE;
		                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("UP")) {
		                				ship.state = NetworkShip.STATE.UP;
		                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("LEFT")) {
		                				ship.state = NetworkShip.STATE.LEFT;
		                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("RIGHT")) {
		                				ship.state = NetworkShip.STATE.RIGHT;
		                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("UPLEFT")) {
		                				ship.state = NetworkShip.STATE.UPLEFT;
		                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("UPRIGHT")) {
		                				ship.state = NetworkShip.STATE.UPRIGHT;
		                			}
		                			
		                			if(obj.getJSONObject("message").getInt("fire") == 1) {		                				
	                					gameSession.shootEnemy(ship.body.getWorldCenter().add(ship.body.getWorldVector(new Vector2(3f,1f))), ship.body.getWorldVector(new Vector2(0,1f)).rotate(-90).cpy());
	                					ship.hitAnimation = -4;		                				
		                			} else if(obj.getJSONObject("message").getInt("fire") == -1) {
		                				gameSession.shootEnemy(ship.body.getWorldCenter().add(ship.body.getWorldVector(new Vector2(-3f,1f))), ship.body.getWorldVector(new Vector2(0,1f)).rotate(90).cpy());
		                				ship.hitAnimation = 4;
		                			}
		                		}
		                	}
		                }
		                if (event.equals("synchronize")) {
		                	System.out.println("synchronize");
		                	for(NetworkShip ship:enemies) {
		                		if(ship.id.equalsIgnoreCase(obj.getString("player"))) {
		                			System.out.println("synchronize " + ship.id);	
		                			Vector2 networkPos = new Vector2((float) obj.getJSONObject("message").getDouble("positionx"),(float) obj.getJSONObject("message").getDouble("positiony"));
		                			if(networkPos.dst(ship.body.getPosition())>1) {		                				
		                				gameSession.networkUpdates.add(new UpdatePackage(ship, networkPos, (float)obj.getJSONObject("message").getDouble("angle")));
		                			} else {
			                			networkPos.sub(ship.body.getPosition());
			                			Vector2 newPos = ship.body.getPosition().tmp().add(networkPos.mul(0.1f));
			                			gameSession.networkUpdates.add(new UpdatePackage(ship, newPos, (float)obj.getJSONObject("message").getDouble("angle")));
		                			}	
		                			
		                			if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("IDLE")) {
		                				ship.state = NetworkShip.STATE.IDLE;
		                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("UP")) {
		                				ship.state = NetworkShip.STATE.UP;
		                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("LEFT")) {
		                				ship.state = NetworkShip.STATE.LEFT;
		                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("RIGHT")) {
		                				ship.state = NetworkShip.STATE.RIGHT;
		                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("UPLEFT")) {
		                				ship.state = NetworkShip.STATE.UPLEFT;
		                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("UPRIGHT")) {
		                				ship.state = NetworkShip.STATE.UPRIGHT;
		                			}
		                		}
		                	}
		                }
		                
		                if (event.equals("hit")) {
		                	System.out.println("hit");
		                	addMessage("player " + obj.getString("player") + " hit");
		                	for(NetworkShip ship:enemies) {
		                		if(ship.id.equalsIgnoreCase(obj.getString("player"))) {
		                			System.out.println("hit " + ship.id);	
		                			ship.life = ship.life - 1; 
		                			
		                			//sync ship
		                			Vector2 networkPos = new Vector2((float) obj.getJSONObject("message").getDouble("positionx"),(float) obj.getJSONObject("message").getDouble("positiony"));
		                			networkPos.sub(ship.body.getPosition());
		                			Vector2 newPos = ship.body.getPosition().tmp().add(networkPos.mul(0.1f));
		                			
		                			gameSession.networkUpdates.add(new UpdatePackage(ship, newPos, (float)obj.getJSONObject("message").getDouble("angle")));
		                			
		                			if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("IDLE")) {
		                				ship.state = NetworkShip.STATE.IDLE;
		                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("UP")) {
		                				ship.state = NetworkShip.STATE.UP;
		                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("LEFT")) {
		                				ship.state = NetworkShip.STATE.LEFT;
		                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("RIGHT")) {
		                				ship.state = NetworkShip.STATE.RIGHT;
		                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("UPLEFT")) {
		                				ship.state = NetworkShip.STATE.UPLEFT;
		                			} else if(obj.getJSONObject("message").getString("state").equalsIgnoreCase("UPRIGHT")) {
		                				ship.state = NetworkShip.STATE.UPRIGHT;
		                			}
		                		}
		                	}
		                }
		                
		                if (event.equals("startround")) {
		                	System.out.println("startround");
		                	gameSession.startNewRound();
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
	
	public void sendCurrentState(Player player, int fire) {		
		if(currentState == player.state && fire == 0) return;
		System.out.println("send update");
		
		JSONObject json = new JSONObject();
        try {
        	json.putOpt("fire", fire);
			json.putOpt("state", player.state);
			json.putOpt("positionx", player.body.getPosition().x);
			json.putOpt("positiony", player.body.getPosition().y);
			json.putOpt("angle", player.body.getAngle());
			json.putOpt("angledir", player.body.getAngularVelocity());
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
			json.putOpt("positionx", player.body.getPosition().x);
			json.putOpt("positiony", player.body.getPosition().y);
			json.putOpt("angle", player.body.getAngle());
			json.putOpt("angledir", player.body.getAngularVelocity());
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
			json.putOpt("positionx", player.body.getPosition().x);
			json.putOpt("positiony", player.body.getPosition().y);
			json.putOpt("angle", player.body.getAngle());
			json.putOpt("angledir", player.body.getAngularVelocity());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		socket.emit("hit", json);			
	}
	
	public static Network getInstance() {
		if(instance!=null) return instance;
		instance = new Network();		
		return instance;
	}
	
	public void addMessage(String m) {
		if(messageList.size>5) {
			messageList.removeIndex(0);
		}
		messageList.add(m);		
	}

	public void setGameSession(MultiPlayerScreen multiPlayerScreen) {
		this.gameSession = multiPlayerScreen; 
	}

}
