package de.redlion.badminton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import de.redlion.badminton.shader.DiffuseShader;

public class Resources {
	
	public final boolean debugMode = true;

	public BitmapFont font;
	
	public Sprite background = new Sprite(new Texture(Gdx.files.internal("data/court_bg.png")));
	public Sprite playerSprite = new Sprite(new Texture(Gdx.files.internal("data/player.png")));
	public Texture lock = new Texture(Gdx.files.internal("data/lock.png"));
	
	public ShaderProgram diffuseShader;
	
	public static Resources instance;

	public static Resources getInstance() {
		if (instance == null) {
			instance = new Resources();
		}
		return instance;
	}

	public Resources() {		
		reInit();	
	}
	
	public void initShader() {
		diffuseShader = new ShaderProgram(DiffuseShader.mVertexShader, DiffuseShader.mFragmentShader);
		if (diffuseShader.isCompiled() == false) {
			Gdx.app.log("diffuseShader: ", diffuseShader.getLog());
			System.exit(0);
		}
	}

	public void reInit() {	
		initShader();
		
		font = new BitmapFont();
	}

	public void dispose() {
		font.dispose();
		
		diffuseShader.dispose();
	}
}
