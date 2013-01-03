package de.redlion.badminton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Resources {
	public BitmapFont font;
	
	public Texture lock = new Texture(Gdx.files.internal("data/lock.png"));
	
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
	

	public void reInit() {
		font = new BitmapFont();
	}

	public void dispose() {
		font.dispose();
	}
}
