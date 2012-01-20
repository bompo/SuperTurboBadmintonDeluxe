package de.redlion.badminton;

import java.io.IOException;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;


public class GameScreen extends DefaultScreen implements InputProcessor {

	float startTime = 0;
	OrthographicCamera cam;

	SpriteBatch batch;
	SpriteBatch fadeBatch;
	SpriteBatch fontbatch;
	BitmapFont font;
	Sprite blackFade;
	
	BackgroundFXRenderer bg;

	Player player = new Player();

	float fade = 1.0f;
	boolean finished = false;
	
	float footStepCnt = 0;

	float delta;
	
	private boolean win = false;

	public GameScreen(Game game) {
		super(game);
		Gdx.input.setInputProcessor(this);
				
		bg = new BackgroundFXRenderer();
		
		batch = new SpriteBatch();
		batch.getProjectionMatrix().setToOrtho2D(0, 0, 800, 480);
		fontbatch = new SpriteBatch();

		blackFade = new Sprite(new Texture(Gdx.files.internal("data/black.png")));
		fadeBatch = new SpriteBatch();
		fadeBatch.getProjectionMatrix().setToOrtho2D(0, 0, 1, 1);

		font = Resources.getInstance().font;
		font.setScale(1);
		
		initRender();
		initLevel();
	}
	

	public void initRender() {
		Gdx.graphics.getGL20().glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		Vector3 oldPosition = new Vector3();
		Vector3 oldDirection = new Vector3();
		if(cam!=null) {
			oldPosition.set(cam.position);
			oldDirection.set(cam.direction);
			cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			cam.position.set(oldPosition);
		} else {
			cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			cam.position.set(0, 0f, 0f);
		}
		
		initRender();
	}

	private void initLevel() {		
	}

	private void reset() {
		initLevel();
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		delta = Math.min(0.1f, deltaTime);

		startTime += delta;
		
		cam.update();

		collisionTest();		
		updateAI();		
		renderScene();
		
		batch.begin();
			font.draw(batch, Gdx.graphics.getFramesPerSecond() + " fps" ,20, 30);		
		batch.end();
		

		// FadeInOut
		if (!finished && fade > 0) {
			fade = Math.max(fade - (delta), 0);
			fadeBatch.begin();
			blackFade.setColor(blackFade.getColor().r, blackFade.getColor().g, blackFade.getColor().b, fade);
			blackFade.draw(fadeBatch);
			fadeBatch.end();
		}

		if (finished) {
			fade = Math.min(fade + (delta), 1);
			fadeBatch.begin();
			blackFade.setColor(blackFade.getColor().r, blackFade.getColor().g, blackFade.getColor().b, fade);
			blackFade.draw(fadeBatch);
			fadeBatch.end();
			if (fade >= 1) {
				Gdx.app.exit();
			}
		}
	}

	private void updateAI() {
		cam.position.y = player.getY()/5;
		player.update();
	}

	private void renderScene() {
		bg.render(cam);
		
		batch.begin();
			player.draw(batch);
		batch.end();
	}
	
	private void processInput() {	        
	}

	private void collisionTest() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean keyDown(int keycode) {
		if (Gdx.input.isTouched())
			return false;
		if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
			finished = true;
		}
		if (keycode == Input.Keys.F) {
			if(Gdx.app.getType() == ApplicationType.Desktop) {
				if(!org.lwjgl.opengl.Display.isFullscreen()) {
					Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);
					Configuration.getInstance().setFullscreen(true);
				} else {
					Gdx.graphics.setDisplayMode(800,480, false);		
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
		
		//Player controls
		if (keycode == Input.Keys.LEFT) {
			player.state = Player.STATE.LEFT;
		}	
		if (keycode == Input.Keys.RIGHT) {
			player.state = Player.STATE.RIGHT;
		}
		if (keycode == Input.Keys.UP) {
			player.state = Player.STATE.FORWARD;
		}	
		if (keycode == Input.Keys.DOWN) {
			player.state = Player.STATE.BACKWARD;
		}	
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.LEFT) {
			player.state = Player.STATE.IDLE;
		}	
		if (keycode == Input.Keys.RIGHT) {
			player.state = Player.STATE.IDLE;
		}
		if (keycode == Input.Keys.UP) {
			player.state = Player.STATE.IDLE;
		}	
		if (keycode == Input.Keys.DOWN) {
			player.state = Player.STATE.IDLE;
		}	
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
	public boolean touchMoved(int x, int y) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
