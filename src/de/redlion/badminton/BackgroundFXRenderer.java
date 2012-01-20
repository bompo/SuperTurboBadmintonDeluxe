package de.redlion.badminton;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class BackgroundFXRenderer {

	SpriteBatch backgroundFXBatch;	
	SpriteBatch backgroundBatch;
	Sprite background;	
	
	Vector2 position = new Vector2();

	public BackgroundFXRenderer() {
		backgroundFXBatch = new SpriteBatch();
		backgroundFXBatch.getProjectionMatrix().setToOrtho2D(0, 0, 800, 480);
		
		background = Resources.getInstance().background;
		backgroundBatch = new SpriteBatch();
		backgroundBatch.getProjectionMatrix().setToOrtho2D(0, 0, 800, 480);
	}


	public void render(OrthographicCamera cam) {		
		backgroundBatch.getProjectionMatrix().setToOrtho2D(0, cam.position.y, 800, 480);
		backgroundBatch.begin();
		background.setPosition(position.x, position.y);
		background.draw(backgroundBatch);
		backgroundBatch.end();
	}
	
	public void resize(int width, int height) {
		backgroundFXBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}
	
	public void dispose() {
		backgroundFXBatch.dispose();
		backgroundBatch.dispose();
	}
	
}
