package de.redlion.badminton.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import de.redlion.badminton.GameSession;
import de.redlion.badminton.Resources;

public class RenderDebug {

	SpriteBatch batch;
	BitmapFont font;
	
	ShapeRenderer renderer;
	
	public RenderDebug() {
		batch = new SpriteBatch();
		batch.getProjectionMatrix().setToOrtho2D(0, 0, 800, 480);
		font = Resources.getInstance().font;
		font.setScale(1);
		
		renderer = new ShapeRenderer();
	}
	
	

	public void render(PerspectiveCamera cam) {
		batch.begin();
		font.draw(batch, Gdx.graphics.getFramesPerSecond() + " fps", 20, 30);
		font.draw(batch, GameSession.getInstance().player.toString(), 20, 50);
		font.draw(batch, GameSession.getInstance().opponent.toString(), 20, 70);
		font.draw(batch, GameSession.getInstance().birdie.toString(), 20, 90);
		batch.end();
		
		renderer.setProjectionMatrix(cam.combined);
		renderer.begin(ShapeType.Line);
		
		//draw court borders
		renderer.line(GameSession.getInstance().borders.min.x, GameSession.getInstance().borders.min.y, GameSession.getInstance().borders.min.z,
				GameSession.getInstance().borders.min.x, GameSession.getInstance().borders.max.y, GameSession.getInstance().borders.max.z);
		renderer.line(GameSession.getInstance().borders.max.x, GameSession.getInstance().borders.min.y, GameSession.getInstance().borders.min.z,
				GameSession.getInstance().borders.max.x, GameSession.getInstance().borders.max.y, GameSession.getInstance().borders.max.z);
		renderer.line(GameSession.getInstance().borders.min.x, GameSession.getInstance().borders.min.y, GameSession.getInstance().borders.min.z,
				GameSession.getInstance().borders.max.x, GameSession.getInstance().borders.max.y, GameSession.getInstance().borders.min.z);
		renderer.line(GameSession.getInstance().borders.min.x, GameSession.getInstance().borders.min.y, GameSession.getInstance().borders.max.z,
				GameSession.getInstance().borders.max.x, GameSession.getInstance().borders.max.y, GameSession.getInstance().borders.max.z);
		
		//draw player
		renderer.line(GameSession.getInstance().player.position.x - 1f, GameSession.getInstance().player.position.y, GameSession.getInstance().player.position.z - 1f, 
				GameSession.getInstance().player.position.x - 1f, GameSession.getInstance().player.position.y, GameSession.getInstance().player.position.z + 1f);
		renderer.line(GameSession.getInstance().player.position.x + 1f, GameSession.getInstance().player.position.y, GameSession.getInstance().player.position.z - 1f, 
				GameSession.getInstance().player.position.x + 1f, GameSession.getInstance().player.position.y, GameSession.getInstance().player.position.z + 1f);
		renderer.line(GameSession.getInstance().player.position.x + 1f, GameSession.getInstance().player.position.y, GameSession.getInstance().player.position.z + 1f, 
				GameSession.getInstance().player.position.x - 1f, GameSession.getInstance().player.position.y, GameSession.getInstance().player.position.z + 1f);
		renderer.line(GameSession.getInstance().player.position.x + 1f, GameSession.getInstance().player.position.y, GameSession.getInstance().player.position.z - 1f, 
				GameSession.getInstance().player.position.x - 1f, GameSession.getInstance().player.position.y, GameSession.getInstance().player.position.z - 1f);
		
		//draw opponent
		renderer.line(GameSession.getInstance().opponent.position.x - 1f, GameSession.getInstance().opponent.position.y, GameSession.getInstance().opponent.position.z - 1f, 
				GameSession.getInstance().opponent.position.x - 1f, GameSession.getInstance().opponent.position.y, GameSession.getInstance().opponent.position.z + 1f);
		renderer.line(GameSession.getInstance().opponent.position.x + 1f, GameSession.getInstance().opponent.position.y, GameSession.getInstance().opponent.position.z - 1f, 
				GameSession.getInstance().opponent.position.x + 1f, GameSession.getInstance().opponent.position.y, GameSession.getInstance().opponent.position.z + 1f);
		renderer.line(GameSession.getInstance().opponent.position.x + 1f, GameSession.getInstance().opponent.position.y, GameSession.getInstance().opponent.position.z + 1f, 
				GameSession.getInstance().opponent.position.x - 1f, GameSession.getInstance().opponent.position.y, GameSession.getInstance().opponent.position.z + 1f);
		renderer.line(GameSession.getInstance().opponent.position.x + 1f, GameSession.getInstance().opponent.position.y, GameSession.getInstance().opponent.position.z - 1f, 
				GameSession.getInstance().opponent.position.x - 1f, GameSession.getInstance().opponent.position.y, GameSession.getInstance().opponent.position.z - 1f);
		
		renderer.line(GameSession.getInstance().birdie.currentPosition.x - 0.1f, GameSession.getInstance().birdie.currentPosition.y, GameSession.getInstance().birdie.currentPosition.z - 0.1f, 
				GameSession.getInstance().birdie.currentPosition.x - 0.1f, GameSession.getInstance().birdie.currentPosition.y, GameSession.getInstance().birdie.currentPosition.z + 0.1f);
		renderer.line(GameSession.getInstance().birdie.currentPosition.x + 0.1f, GameSession.getInstance().birdie.currentPosition.y, GameSession.getInstance().birdie.currentPosition.z - 0.1f, 
				GameSession.getInstance().birdie.currentPosition.x + 0.1f, GameSession.getInstance().birdie.currentPosition.y, GameSession.getInstance().birdie.currentPosition.z + 0.1f);
		renderer.line(GameSession.getInstance().birdie.currentPosition.x + 0.1f, GameSession.getInstance().birdie.currentPosition.y, GameSession.getInstance().birdie.currentPosition.z + 0.1f, 
				GameSession.getInstance().birdie.currentPosition.x - 0.1f, GameSession.getInstance().birdie.currentPosition.y, GameSession.getInstance().birdie.currentPosition.z + 0.1f);
		renderer.line(GameSession.getInstance().birdie.currentPosition.x + 0.1f, GameSession.getInstance().birdie.currentPosition.y, GameSession.getInstance().birdie.currentPosition.z - 0.1f, 
				GameSession.getInstance().birdie.currentPosition.x - 0.1f, GameSession.getInstance().birdie.currentPosition.y, GameSession.getInstance().birdie.currentPosition.z - 0.1f);
		
		renderer.end();

	}
	
	public void resize(int width, int height) {
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);

	}

}
