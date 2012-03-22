package de.redlion.badminton;

import java.io.IOException;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoaderRegistry;
import com.badlogic.gdx.graphics.g3d.model.still.StillModel;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import de.redlion.badminton.Birdie.STATE;


public class GameScreen extends DefaultScreen implements InputProcessor {

	float startTime = 0;
	PerspectiveCamera cam;

	SpriteBatch batch;
	SpriteBatch fadeBatch;
	SpriteBatch fontbatch;
	BitmapFont font;
	Sprite blackFade;
	
	StillModel modelPlaneObj;
	Texture modelPlaneTex;

	Player player = new Player();
	Birdie birdie = new Birdie();
	Opponent opp = new Opponent(birdie);

	float fade = 1.0f;
	boolean finished = false;
	
	float footStepCnt = 0;

	float delta;

	
	// GLES20
	Matrix4 model = new Matrix4().idt();
	Matrix4 normal = new Matrix4().idt();
	Matrix4 tmp = new Matrix4().idt();
	
	private ShaderProgram diffuseShader;
	
	private boolean win = false;

	public GameScreen(Game game) {
		super(game);
		Gdx.input.setInputProcessor(this);
				
		batch = new SpriteBatch();
		batch.getProjectionMatrix().setToOrtho2D(0, 0, 800, 480);
		fontbatch = new SpriteBatch();

		blackFade = new Sprite(new Texture(Gdx.files.internal("data/black.png")));
		fadeBatch = new SpriteBatch();
		fadeBatch.getProjectionMatrix().setToOrtho2D(0, 0, 1, 1);

		modelPlaneObj = ModelLoaderRegistry.loadStillModel(Gdx.files.internal("data/plane.g3dt"));	
		modelPlaneTex = new Texture(Gdx.files.internal("data/court_top_texture.png"));
		modelPlaneTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		diffuseShader = Resources.getInstance().diffuseShader;	
		
		font = Resources.getInstance().font;
		font.setScale(1);
		
		initRender();
		initLevel();
	}
	

	public void initRender() {
		Gdx.graphics.getGL20().glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		Vector3 oldPosition = new Vector3();
		Vector3 oldDirection = new Vector3();
		if(cam!=null) {
			oldPosition.set(cam.position);
			oldDirection.set(cam.direction);
			cam = new PerspectiveCamera(7, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			cam.position.set(oldPosition);
			cam.lookAt(0, 0, 0.5f);
		} else {
			cam = new PerspectiveCamera(7, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			cam.position.set(-0.6f, 7.2f, 38.8f);
			cam.lookAt(0, -0.2f, -1.1f);
		}
		cam.up.set(0, 1, 0);
		cam.near = 0.5f;
		cam.far = 600f;
		
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
		player.update();
		birdie.update();
		opp.update(player.position);
		
		if(opp.position.epsilonEquals(birdie.position, 1.3f) && birdie.state != Birdie.STATE.HITBYOPPONENT) {
			birdie.state  = Birdie.STATE.HITBYOPPONENT;
			birdie.hit(opp.direction);
		}
	}

	private void renderScene() {	
		
		//3D Stuff
		diffuseShader.begin();
		diffuseShader.setUniformMatrix("VPMatrix", cam.combined);
		diffuseShader.setUniformi("uSampler", 0);
		
		// render court
		tmp.idt();
		model.idt();

		tmp.setToTranslation(0,0, 0);
		model.mul(tmp);
		
		tmp.setToScaling(10,10,10);
		model.mul(tmp);
			
		diffuseShader.setUniformMatrix("MMatrix", model);
		diffuseShader.setUniformi("uSampler", 0);
		
		modelPlaneTex.bind(0);
		modelPlaneObj.render(diffuseShader);
		
		
		// render birdie
		tmp.idt();
		model.idt();
		
		tmp.setToRotation(Vector3.X, 90);
		model.mul(tmp);

		tmp.setToTranslation(birdie.position.x,birdie.position.y, birdie.position.z-0.1f);
		model.mul(tmp);
		
		tmp.setToScaling(0.1f,0.1f,0.1f);
		model.mul(tmp);
			
		diffuseShader.setUniformMatrix("MMatrix", model);
		diffuseShader.setUniformi("uSampler", 0);
		
		modelPlaneTex.bind(0);
		modelPlaneObj.render(diffuseShader);
		
		// render shadow
		tmp.idt();
		model.idt();
		
		tmp.setToRotation(Vector3.X, 90);
		model.mul(tmp);

		tmp.setToTranslation(birdie.position.x,birdie.position.y, 0.0f);
		model.mul(tmp);
		
		tmp.setToScaling(0.1f,0.1f,0.1f);
		model.mul(tmp);
			
		diffuseShader.setUniformMatrix("MMatrix", model);
		diffuseShader.setUniformi("uSampler", 0);
		
		modelPlaneTex.bind(0);
		modelPlaneObj.render(diffuseShader);
		
		
		// render player
		tmp.idt();
		model.idt();
		
		tmp.setToRotation(Vector3.X, 90);
		model.mul(tmp);

		if(!player.jump)
			tmp.setToTranslation(player.position.x,player.position.y, -0.5f);
		else
			tmp.setToTranslation(player.position.x,player.position.y, player.position.z);
		model.mul(tmp);
		
		tmp.setToScaling(0.5f,0.5f,0.5f);
		model.mul(tmp);
			
		diffuseShader.setUniformMatrix("MMatrix", model);
		diffuseShader.setUniformi("uSampler", 0);
		
		modelPlaneTex.bind(0);
		modelPlaneObj.render(diffuseShader);
		
		
		
		// render opponent
		tmp.idt();
		model.idt();
		
		tmp.setToRotation(Vector3.X, 90);
		model.mul(tmp);

		if(!opp.jump)
			tmp.setToTranslation(opp.position.x,opp.position.y, -0.5f);
		else
			tmp.setToTranslation(opp.position.x,opp.position.y, opp.position.z);
		model.mul(tmp);
		
		tmp.setToScaling(0.5f,0.5f,0.5f);
		model.mul(tmp);
			
		diffuseShader.setUniformMatrix("MMatrix", model);
		diffuseShader.setUniformi("uSampler", 0);
		
		modelPlaneTex.bind(0);
		modelPlaneObj.render(diffuseShader);
		
		diffuseShader.end();		
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
		if (keycode == Input.Keys.A) {
			if(player.state == Player.STATE.IDLE)
				player.state = Player.STATE.LEFT;
			else if(player.state == Player.STATE.DOWN)
				player.state = Player.STATE.DOWNLEFT;
			else if(player.state == Player.STATE.DOWNLEFT)
				player.state = Player.STATE.DOWNLEFT;
			else if(player.state == Player.STATE.DOWNRIGHT)
				player.state = Player.STATE.DOWNLEFT;
			else if(player.state == Player.STATE.LEFT)
				player.state = Player.STATE.LEFT;
			else if(player.state == Player.STATE.RIGHT)
				player.state = Player.STATE.IDLE;
			else if(player.state == Player.STATE.UP)
				player.state = Player.STATE.UPLEFT;
			else if(player.state == Player.STATE.UPLEFT)
				player.state = Player.STATE.UPLEFT;
			else if(player.state == Player.STATE.UPRIGHT)
				player.state = Player.STATE.UPLEFT;
		}
		if (keycode == Input.Keys.D) {
			if(player.state == Player.STATE.IDLE)
				player.state = Player.STATE.RIGHT;
			else if(player.state == Player.STATE.DOWN)
				player.state = Player.STATE.DOWNRIGHT;
			else if(player.state == Player.STATE.DOWNLEFT)
				player.state = Player.STATE.DOWNRIGHT;
			else if(player.state == Player.STATE.DOWNRIGHT)
				player.state = Player.STATE.DOWNRIGHT;
			else if(player.state == Player.STATE.LEFT)
				player.state = Player.STATE.IDLE;
			else if(player.state == Player.STATE.RIGHT)
				player.state = Player.STATE.RIGHT;
			else if(player.state == Player.STATE.UP)
				player.state = Player.STATE.UPRIGHT;
			else if(player.state == Player.STATE.UPLEFT)
				player.state = Player.STATE.UPRIGHT;
			else if(player.state == Player.STATE.UPRIGHT)
				player.state = Player.STATE.UPRIGHT;
		}
		if (keycode == Input.Keys.W) {
			if(player.state == Player.STATE.IDLE)
				player.state = Player.STATE.UP;
			else if(player.state == Player.STATE.DOWN)
				player.state = Player.STATE.IDLE;
			else if(player.state == Player.STATE.DOWNLEFT)
				player.state = Player.STATE.UPLEFT;
			else if(player.state == Player.STATE.DOWNRIGHT)
				player.state = Player.STATE.UPRIGHT;
			else if(player.state == Player.STATE.LEFT)
				player.state = Player.STATE.UPLEFT;
			else if(player.state == Player.STATE.RIGHT)
				player.state = Player.STATE.UPRIGHT;
			else if(player.state == Player.STATE.UP)
				player.state = Player.STATE.UP;
			else if(player.state == Player.STATE.UPLEFT)
				player.state = Player.STATE.UPLEFT;
			else if(player.state == Player.STATE.UPRIGHT)
				player.state = Player.STATE.UPRIGHT;
		}	
		if (keycode == Input.Keys.S) {
			if(player.state == Player.STATE.IDLE)
				player.state = Player.STATE.DOWN;
			else if(player.state == Player.STATE.DOWN)
				player.state = Player.STATE.DOWN;
			else if(player.state == Player.STATE.DOWNLEFT)
				player.state = Player.STATE.DOWNLEFT;
			else if(player.state == Player.STATE.DOWNRIGHT)
				player.state = Player.STATE.DOWNRIGHT;
			else if(player.state == Player.STATE.LEFT)
				player.state = Player.STATE.DOWNLEFT;
			else if(player.state == Player.STATE.RIGHT)
				player.state = Player.STATE.DOWNRIGHT;
			else if(player.state == Player.STATE.UP)
				player.state = Player.STATE.IDLE;
			else if(player.state == Player.STATE.UPLEFT)
				player.state = Player.STATE.DOWNLEFT;
			else if(player.state == Player.STATE.UPRIGHT)
				player.state = Player.STATE.DOWNRIGHT;
		}	
		if (keycode == Input.Keys.CONTROL_LEFT) {
			if(player.position.epsilonEquals(birdie.position, 1.3f) && birdie.state != Birdie.STATE.HIT) {
				birdie.state = Birdie.STATE.HIT;
				birdie.hit(player.direction);
			}
		}
		if (keycode == Input.Keys.SPACE) {
			if(!player.jump)
				player.jump();
		}
		if (keycode == Input.Keys.R) {
			birdie.position = new Vector3(2,5,-0.5f);
		}
//		if (keycode == Input.Keys.S) {
//			cam.position.z += 0.1;
//		}
//		if (keycode == Input.Keys.W) {
//			cam.position.z -= 0.1;
//		}
//		if (keycode == Input.Keys.A) {
//			cam.position.x += 0.1;
//		}
//		if (keycode == Input.Keys.D) {
//			cam.position.x -= 0.1;
//		}
//		if (keycode == Input.Keys.G) {
//			cam.fieldOfView += 0.5;
//		}
//		if (keycode == Input.Keys.H) {
//			cam.fieldOfView -= 0.5;
//		}
//		if (keycode == Input.Keys.Q) {
//			cam.direction.z += 0.1;
//		}
//		if (keycode == Input.Keys.E) {
//			cam.direction.z -= 0.1;
//		}
//		if (keycode == Input.Keys.R) {
//			cam.position.y += 0.1;
//		}
//		if (keycode == Input.Keys.T) {
//			cam.position.y -= 0.1;
//		}
//		if (keycode == Input.Keys.ENTER) {
//			Gdx.app.log("pos: ", cam.position.toString());
//			Gdx.app.log("look: ", cam.direction.toString());
//			Gdx.app.log("fov: ", cam.fieldOfView + "");
//		}
		
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		
		if (keycode == Input.Keys.A) {
			if(Gdx.input.isKeyPressed(Input.Keys.D)) {
				if(Gdx.input.isKeyPressed(Input.Keys.W))
					player.state = Player.STATE.UPRIGHT;
				else if(Gdx.input.isKeyPressed(Input.Keys.S))
					player.state = Player.STATE.DOWNRIGHT;
				else
					player.state = Player.STATE.RIGHT;
			}
			else if(player.state == Player.STATE.DOWN)
				player.state = Player.STATE.DOWN;
			else if(player.state == Player.STATE.DOWNLEFT) {
				if(Gdx.input.isKeyPressed(Input.Keys.D))
					player.state = Player.STATE.DOWNRIGHT;
				else
					player.state = Player.STATE.DOWN;
			}
			else if(player.state == Player.STATE.DOWNRIGHT)
				player.state = Player.STATE.DOWNRIGHT;
			else if(player.state == Player.STATE.LEFT)
				player.state = Player.STATE.IDLE;
			else if(player.state == Player.STATE.RIGHT)
				player.state = Player.STATE.RIGHT;
			else if(player.state == Player.STATE.UP)
				player.state = Player.STATE.UP;
			else if(player.state == Player.STATE.UPLEFT){
				if(Gdx.input.isKeyPressed(Input.Keys.D))
					player.state = Player.STATE.UPRIGHT;
				else
					player.state = Player.STATE.UP;
			}
			else if(player.state == Player.STATE.UPRIGHT)
				player.state = Player.STATE.UPRIGHT;
		}
		if (keycode == Input.Keys.D) {
			if(Gdx.input.isKeyPressed(Input.Keys.A)) {
				if(Gdx.input.isKeyPressed(Input.Keys.W))
					player.state = Player.STATE.UPLEFT;
				else if(Gdx.input.isKeyPressed(Input.Keys.S))
					player.state = Player.STATE.DOWNLEFT;
				else
					player.state = Player.STATE.LEFT;
			}
			if (player.state == Player.STATE.DOWN)
				player.state = Player.STATE.DOWN;
			else if (player.state == Player.STATE.DOWNRIGHT) {
				if (Gdx.input.isKeyPressed(Input.Keys.A))
					player.state = Player.STATE.DOWNLEFT;
				else
					player.state = Player.STATE.DOWN;
			} else if (player.state == Player.STATE.DOWNLEFT)
				player.state = Player.STATE.DOWNLEFT;
			else if (player.state == Player.STATE.RIGHT)
				player.state = Player.STATE.IDLE;
			else if (player.state == Player.STATE.LEFT)
				player.state = Player.STATE.LEFT;
			else if (player.state == Player.STATE.UP)
				player.state = Player.STATE.UP;
			else if (player.state == Player.STATE.UPRIGHT) {
				if (Gdx.input.isKeyPressed(Input.Keys.A))
					player.state = Player.STATE.UPLEFT;
				else
					player.state = Player.STATE.UP;
			} else if (player.state == Player.STATE.UPLEFT)
				player.state = Player.STATE.UPLEFT;
		}
		if (keycode == Input.Keys.W) {
			if(Gdx.input.isKeyPressed(Input.Keys.S)) {
				if(Gdx.input.isKeyPressed(Input.Keys.A))
					player.state = Player.STATE.DOWNLEFT;
				else if(Gdx.input.isKeyPressed(Input.Keys.D))
					player.state = Player.STATE.DOWNRIGHT;
				else
					player.state = Player.STATE.DOWN;
			}
			if (player.state == Player.STATE.DOWN)
				player.state = Player.STATE.DOWN;
			else if (player.state == Player.STATE.DOWNLEFT)
				player.state = Player.STATE.DOWNLEFT;
			else if (player.state == Player.STATE.DOWNRIGHT)
				player.state = Player.STATE.DOWNRIGHT;
			else if (player.state == Player.STATE.LEFT)
				player.state = Player.STATE.LEFT;
			else if (player.state == Player.STATE.RIGHT)
				player.state = Player.STATE.RIGHT;
			else if (player.state == Player.STATE.UP)
				player.state = Player.STATE.IDLE;
			else if (player.state == Player.STATE.UPLEFT) {
				if (Gdx.input.isKeyPressed(Input.Keys.S))
					player.state = Player.STATE.DOWNLEFT;
				else
					player.state = Player.STATE.LEFT;
			} else if (player.state == Player.STATE.UPRIGHT) {
				if (Gdx.input.isKeyPressed(Input.Keys.S))
					player.state = Player.STATE.DOWNRIGHT;
				else
					player.state = Player.STATE.RIGHT;
			}
		}
		if (keycode == Input.Keys.S ) {
			if(Gdx.input.isKeyPressed(Input.Keys.W)) {
				if(Gdx.input.isKeyPressed(Input.Keys.A))
					player.state = Player.STATE.UPLEFT;
				else if(Gdx.input.isKeyPressed(Input.Keys.D))
					player.state = Player.STATE.UPRIGHT;
				else
					player.state = Player.STATE.UP;
			}
			if (player.state == Player.STATE.DOWN)
				player.state = Player.STATE.IDLE;
			else if (player.state == Player.STATE.UPLEFT)
				player.state = Player.STATE.UPLEFT;
			else if (player.state == Player.STATE.UPRIGHT)
				player.state = Player.STATE.UPRIGHT;
			else if (player.state == Player.STATE.LEFT)
				player.state = Player.STATE.LEFT;
			else if (player.state == Player.STATE.RIGHT)
				player.state = Player.STATE.RIGHT;
			else if (player.state == Player.STATE.UP)
				player.state = Player.STATE.UP;
			else if (player.state == Player.STATE.DOWNLEFT) {
				if (Gdx.input.isKeyPressed(Input.Keys.W))
					player.state = Player.STATE.UPLEFT;
				else
					player.state = Player.STATE.LEFT;
			} else if (player.state == Player.STATE.DOWNRIGHT) {
				if (Gdx.input.isKeyPressed(Input.Keys.W))
					player.state = Player.STATE.UPRIGHT;
				else
					player.state = Player.STATE.RIGHT;
			}
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
