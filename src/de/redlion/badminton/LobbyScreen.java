package de.redlion.badminton;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.FlickScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;

import de.redlion.badminton.MenuScreen.MODE;

import de.redlion.badminton.network.Network;
import de.redlion.badminton.network.Room;


public class LobbyScreen extends DefaultScreen implements InputProcessor {

	public enum MODE {
		TITLESCREEN,SINGLEPLAYER,MULTIPLAYER,EXIT;
	}
	
	MODE mode = MODE.EXIT;
	
	float startTime = 0;
	PerspectiveCamera cam;

	SpriteBatch batch;
	SpriteBatch fadeBatch;
	SpriteBatch fontbatch;
	BitmapFont font;
	Sprite blackFade;
	
	Stage ui;
	Stage createRoomMenu;
	boolean creating = false;
	boolean waiting = false;
	Table container;
	Table table;
	Skin skin;
	
	int roomCnt = 0;

	float fade = 1.0f;
	boolean finished = false;
	float delta;

	public LobbyScreen(Game game) {
		super(game);
		ui = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		createRoomMenu = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		
		Gdx.input.setInputProcessor(ui);

		batch = new SpriteBatch();
		batch.getProjectionMatrix().setToOrtho2D(0, 0, 800, 480);
		fontbatch = new SpriteBatch();

		blackFade = new Sprite(
				new Texture(Gdx.files.internal("data/black.png")));
		fadeBatch = new SpriteBatch();
		fadeBatch.getProjectionMatrix().setToOrtho2D(0, 0, 1, 1);

		font = Resources.getInstance().font;
		font.setScale(1);
		
		skin = new Skin(Gdx.files.internal("data/uiskin.json"), Gdx.files.internal("data/uiskin.png"));
		
		//ui stuff	
		container = new Table();
		container.setSkin(skin);
		ui.addActor(container);
		container.getTableLayout().debug();

		table = new Table();
		table.setSkin(skin);
		FlickScrollPane scroll = new FlickScrollPane(table);
		container.add(scroll).expand().fill();
		table.parse("pad:10 * expand:x space:4");

		
		
		
		container.getTableLayout().row().left();
		TextButton createRoom = new TextButton("Create Room", skin);
		createRoom.setClickListener(new ClickListener() {
			
			@Override
			public void click(Actor arg0, float arg1, float arg2) {
				
				final Table temp = new Table(skin);
				temp.width = 300;
				temp.height = 150;
				temp.y = Gdx.graphics.getHeight() / 2 - temp.height / 2;
				temp.x = Gdx.graphics.getWidth() / 2 - temp.width / 2;

				temp.setBackground(new NinePatch(blackFade));
				createRoomMenu.addActor(temp);
				
				final TextField name = new TextField("Name", skin);
				final TextField pass = new TextField("Password", skin);
				pass.visible = false;
				
				final CheckBox check = new CheckBox("Private", skin);
				check.setClickListener(new ClickListener() {					
					@Override
					public void click(Actor arg0, float arg1, float arg2) {						
						pass.visible = !pass.visible;						
					}
				});
						
				final TextButton cancel = new TextButton("Cancel", skin);
				cancel.setClickListener(new ClickListener() {
					
					@Override
					public void click(Actor arg0, float arg1, float arg2) {
						
						if(waiting) {
							Network.getInstance().sendLeaveRoom();
						}
						
						createRoomMenu.clear();
						Gdx.input.setInputProcessor(ui);
						creating = false;
						waiting = false;
						
					}
				});
				
				TextButton create = new TextButton("Create", skin);
				
				create.setClickListener(new ClickListener() {
					
					@Override
					public void click(Actor arg0, float arg1, float arg2) {

						waiting = true;
						
						
						if(check.isChecked()) {
							Network.getInstance().sendCreatePrivateRoom(name.getText(),pass.getText());
						} else {
							Network.getInstance().sendCreateRoom(name.getText());
						}						
						
						temp.clear();
						
						Label waiting = new Label("Waiting For Challenger...", skin);
						waiting.setColor(1,1,1,1);
						
						temp.add(waiting).top();
						temp.row();
						temp.add(cancel).right();
						
						
						//change to multiplayer
					}
				});
				
				temp.row();
				temp.add(new Label("Create Room", skin)).left();
				temp.row();
				temp.add(name);
				temp.row();
				temp.add(pass).left();
				temp.add(check).right();
				temp.row();
				temp.add(create).left();
				temp.add(cancel);
				
				Gdx.input.setInputProcessor(createRoomMenu);
				
				creating = true;
			}
		});
		

		TextButton back = new TextButton("Back", skin);
		back.setClickListener(new ClickListener() {
			
			@Override
			public void click(Actor arg0, float arg1, float arg2) {
				
				mode = MODE.TITLESCREEN;
				finished = true;
				
			}
		});
		
		Table buttons = new Table(skin);

		container.add(buttons).expandX().fill();
		
		buttons.left();
		buttons.add(createRoom);
		buttons.add(back).expandX().right();
		
		
		
		initRender();
	}

	public void initRender() {
		Gdx.graphics.getGL20().glViewport(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
		ui.setViewport(width, height, false);
		container.width = width;
        container.height = height;
        
		Vector3 oldPosition = new Vector3();
		Vector3 oldDirection = new Vector3();
		if (cam != null) {
			oldPosition.set(cam.position);
			oldDirection.set(cam.direction);
			cam = new PerspectiveCamera(7, Gdx.graphics.getWidth(),
					Gdx.graphics.getHeight());
			cam.position.set(oldPosition);
			cam.lookAt(0, 0, 0.5f);
		} else {
			cam = new PerspectiveCamera(7, Gdx.graphics.getWidth(),
					Gdx.graphics.getHeight());
			cam.position.set(-0.6f, 7.2f, 38.8f);
			cam.lookAt(0, -0.2f, -1.1f);
		}
		cam.up.set(0, 1, 0);
		cam.near = 0.5f;
		cam.far = 600f;

		initRender();
	}

	@Override
	public void show() {
	}
	
	private float deltaCount = 0;	
	@Override
	public void render(float deltaTime) {
		deltaCount += deltaTime;
		if(deltaCount > 0.01) {
			deltaCount = 0;
			renderFrame(0.02f);
		}
	}

	public void renderFrame(float deltaTime) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		delta = Math.min(0.1f, deltaTime);
		
		checkForNewRooms();
		checkForConnection();

		startTime += delta;

		cam.update();


		// FadeInOut
		if (!finished && fade > 0) {
			fade = Math.max(fade - (delta), 0);
			fadeBatch.begin();
			blackFade.setColor(blackFade.getColor().r, blackFade.getColor().g,
					blackFade.getColor().b, fade);
			blackFade.draw(fadeBatch);
			fadeBatch.end();
		}
		
		ui.act(Gdx.graphics.getDeltaTime());
		ui.draw();
		if(creating) {
			createRoomMenu.act(Gdx.graphics.getDeltaTime());
			createRoomMenu.draw();
		}

		if (finished) {
			fade = Math.min(fade + (delta), 1);
			fadeBatch.begin();
			blackFade.setColor(blackFade.getColor().r, blackFade.getColor().g,
					blackFade.getColor().b, fade);
			blackFade.draw(fadeBatch);
			fadeBatch.end();
			if (fade >= 1) {
				if(mode == MODE.TITLESCREEN) {
					game.setScreen(new MenuScreen(game));
				}
				if(mode == MODE.SINGLEPLAYER) {
					game.setScreen(new SinglePlayerGameScreen(game));
				} 
				if(mode == MODE.MULTIPLAYER) {
					game.setScreen(new MultiPlayerGameScreen(game));
				} 
				if(mode == MODE.EXIT) {
					Gdx.app.exit();
				}
			}
		}
	}

	private void checkForConnection() {
		System.out.println(waiting + " " + Network.getInstance().startGame);
		if(waiting == true && Network.getInstance().startGame == true) {
			//start game
			
			mode = LobbyScreen.MODE.MULTIPLAYER;
			finished = true;
		}
	}

	private void checkForNewRooms() {
		if(roomCnt != Network.getInstance().rooms.size()) {
			//refresh table
			table.reset();
	        int roomNumber = 1;
	        
	        for (final Room room: Network.getInstance().rooms) {
        		if(!room.hasPass) {
        			Label playerCount = new Label(room.playersCnt + "/2", skin);
        			Label roomInfo = new Label("Room " + roomNumber + ": " + room.name, skin);
        			
	        		TextButton join = new TextButton(skin);
	        		
	        		if(room.playersCnt < 2) {
		    			join.setClickListener(new ClickListener() {
		    				
		    				@Override
		    				public void click(Actor arg0, float arg1, float arg2) {
		    					Network.getInstance().sendJoinRoom(room.id);
		    					waiting = true;
		    				}
		    			});
		    			roomInfo.setColor(1,1,1,1);
		    			playerCount.setColor(1,1,1,1);
	        		}
	        		else {
	        			roomInfo.setColor(0,0,0,0.5f);
		    			playerCount.setColor(0,0,0,0.5f);
	        		}
	        		join.getTableLayout().skin = skin;
	        		join.add(roomInfo).center();
	        		join.add(playerCount).right();
	    			table.row();
	    			table.add(join).fill().expandX();
	    			roomNumber++;
        		} else {
        			//private room, check pass with server
        			Label playerCount = new Label(room.playersCnt + "/2", skin);
        			Label roomInfo = new Label("Room " + roomNumber + ": " + room.name + " (private)", skin);
	        		TextButton join = new TextButton(skin);
        			
        			if(room.playersCnt < 2) {
		    			join.setClickListener(new ClickListener() {
		    				
		    				
		    				@Override
		    				public void click(Actor arg0, float arg1, float arg2) {
		    					
		    					System.out.println("join private with pass");
		    					
		    					final Table temp = new Table(skin);
		    					temp.width = 300;
		    					temp.height = 150;
		    					temp.y = Gdx.graphics.getHeight() / 2 - temp.height / 2;
		    					temp.x = Gdx.graphics.getWidth() / 2 - temp.width / 2;

		    					temp.setBackground(new NinePatch(blackFade));
		    					createRoomMenu.addActor(temp);
		    					
		    					final TextField pass = new TextField("Password", skin);
		    					TextButton enter = new TextButton("Enter", skin);
		    					enter.setClickListener(new ClickListener() {
									
									@Override
									public void click(Actor arg0, float arg1, float arg2) {
										waiting = true;
										System.out.println("join private with pass " + pass.getText());
										
									}
								});
		    					
		    					temp.add(pass);
		    					temp.add(enter);
		    					
		    				}
		    			});
	        			roomInfo.setColor(1,1,1,1);
		    			playerCount.setColor(1,1,1,1);
        			}
        			else {
        				roomInfo.setColor(0,0,0,0.5f);
		    			playerCount.setColor(0,0,0,0.5f);
	        		}
        			join.add(roomInfo).center();
        			join.add(playerCount).right();
	    			table.row();
	    			table.add(join).fill().expandX();
	    			roomNumber++;
        		}
	        }
	        roomCnt = Network.getInstance().rooms.size();
		}
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		ui.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (Gdx.input.isTouched())
			return false;
		if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
			mode = MODE.EXIT;
			finished = true;			
		}
		if (keycode == Input.Keys.F) {
			if (Gdx.app.getType() == ApplicationType.Desktop) {
				if (!org.lwjgl.opengl.Display.isFullscreen()) {
					Gdx.graphics.setDisplayMode(
							Gdx.graphics.getDesktopDisplayMode().width,
							Gdx.graphics.getDesktopDisplayMode().height, true);
					Configuration.getInstance().setFullscreen(true);
				} else {
					Gdx.graphics.setDisplayMode(800, 480, false);
					Configuration.getInstance().setFullscreen(false);
				}
			}
		}
		if (keycode == Input.Keys.F8) {
			try {
				ScreenshotSaver.saveScreenshot("screenshot");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

//		// Player controls
//		if (keycode == Input.Keys.NUM_1) {
//			mode = MODE.SINGLEPLAYER;
//			finished = true;
//		}
//		if (keycode == Input.Keys.NUM_2) {
//			mode = MODE.MULTIPLAYER;
//			finished = true;
//		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
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
