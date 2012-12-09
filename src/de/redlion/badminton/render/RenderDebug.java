package de.redlion.badminton.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoaderRegistry;
import com.badlogic.gdx.graphics.g3d.model.still.StillModel;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import de.redlion.badminton.Birdie;
import de.redlion.badminton.GameSession;
import de.redlion.badminton.Helper;
import de.redlion.badminton.Player;
import de.redlion.badminton.Resources;
import de.redlion.badminton.opponent.Opponent;

public class RenderDebug {

	SpriteBatch batch;
	BitmapFont font;

	public RenderDebug() {
		batch = new SpriteBatch();
		batch.getProjectionMatrix().setToOrtho2D(0, 0, 800, 480);
		font = Resources.getInstance().font;
		font.setScale(1);
	}

	public void render() {
		batch.begin();
		font.draw(batch, Gdx.graphics.getFramesPerSecond() + " fps", 20, 30);
		font.draw(batch, GameSession.getInstance().player.toString(), 20, 50);
		font.draw(batch, GameSession.getInstance().opponent.toString(), 20, 70);
		font.draw(batch, GameSession.getInstance().birdie.toString(), 20, 90);
		batch.end();

	}
	
	public void resize(int width, int height) {
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);

	}

}
