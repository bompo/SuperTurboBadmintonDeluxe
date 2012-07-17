package de.redlion.badminton.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
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

public class RenderStadium {

	StillModel modelPlaneObj;
	Texture modelPlaneTex;

	StillModel modelBirdieObj;
	Texture modelBirdieTex;

	StillModel modelCourtObj;
	Texture modelCourtTex;

	StillModel modelStadiumObj;
	Texture modelStadiumTex;

	Texture modelShadowTex;
	
	Birdie birdie;
	Player player;
	Opponent opponent;

	// GLES20
	Matrix4 model = new Matrix4().idt();
	Matrix4 normal = new Matrix4().idt();
	Matrix4 tmp = new Matrix4().idt();
	
	PerspectiveCamera cam;

	private ShaderProgram diffuseShader;

	public RenderStadium() {
		modelPlaneObj = ModelLoaderRegistry.loadStillModel(Gdx.files
				.internal("data/plane.g3dt"));
		modelPlaneTex = new Texture(
				Gdx.files.internal("data/court_top_texture.png"), true);
		modelPlaneTex.setFilter(TextureFilter.MipMapLinearLinear,
				TextureFilter.Linear);
		modelPlaneTex.getTextureData().useMipMaps();

		modelBirdieObj = ModelLoaderRegistry.loadStillModel(Gdx.files
				.internal("data/birdie.g3dt"));
		modelBirdieTex = new Texture(
				Gdx.files.internal("data/birdie_diff.png"), true);
		modelBirdieTex.setFilter(TextureFilter.MipMapLinearLinear,
				TextureFilter.Linear);
		modelBirdieTex.getTextureData().useMipMaps();

		modelCourtObj = ModelLoaderRegistry.loadStillModel(Gdx.files
				.internal("data/court.g3dt"));
		modelCourtTex = new Texture(Gdx.files.internal("data/court_only.png"),
				true);

		modelStadiumObj = ModelLoaderRegistry.loadStillModel(Gdx.files
				.internal("data/stadium.g3dt"));
		modelStadiumTex = new Texture(Gdx.files.internal("data/uv_map.png"),
				true);
		modelStadiumTex.setFilter(TextureFilter.MipMapLinearLinear,
				TextureFilter.Linear);
		modelStadiumTex.getTextureData().useMipMaps();

		modelShadowTex = new Texture(Gdx.files.internal("data/shadow.png"),
				false);

		diffuseShader = Resources.getInstance().diffuseShader;
		
		player = GameSession.getInstance().player;
		birdie = GameSession.getInstance().birdie;
		opponent = GameSession.getInstance().opponent;
	}
	
	public void updateCamera(PerspectiveCamera cam) {
		this.cam = cam;
	}

	public void render() {
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);

		// 3D Stuff
		diffuseShader.begin();
		diffuseShader.setUniformMatrix("VPMatrix", cam.combined);		
		diffuseShader.setUniformi("uSampler", 0);
		diffuseShader.setUniformf("alpha", 1);

		// render court
		tmp.idt();
		model.idt();

		tmp.setToTranslation(0, 0, 0);
		model.mul(tmp);

		tmp.setToScaling(10, 10, 10);
		model.mul(tmp);

		tmp.setToRotation(Vector3.Y, -90);
		model.mul(tmp);

		diffuseShader.setUniformMatrix("MMatrix", model);
		diffuseShader.setUniformi("uSampler", 0);

		modelCourtTex.bind(0);
		modelCourtObj.render(diffuseShader);

		// render birdie
		tmp.idt();
		model.idt();

		tmp.setToRotation(Vector3.X, 90);
		model.mul(tmp);

		tmp.setToTranslation(birdie.currentPosition.x,
				birdie.currentPosition.y, birdie.currentPosition.z);
		model.mul(tmp);

		tmp.setToScaling(0.1f, 0.1f, 0.1f);
		model.mul(tmp);

		tmp.setToLookAt(birdie.tangent, birdie.up);
		model.mul(tmp);

		tmp.setToRotation(Vector3.X, 90);
		model.mul(tmp);

		diffuseShader.setUniformMatrix("MMatrix", model);
		diffuseShader.setUniformi("uSampler", 0);

		modelBirdieTex.bind(0);
		modelBirdieObj.render(diffuseShader);

		// render birdie trajectory
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		if (birdie.trajectoryPath.size > 0) {
			int trajectoryCnt = birdie.trajectoryPath.indexOf(
					birdie.currentPosition, false);
			for (int i = trajectoryCnt; i > trajectoryCnt - 20; i--) {
				if (i > 0) {
					tmp.idt();
					model.idt();

					tmp.setToRotation(Vector3.X, 90);
					model.mul(tmp);

					Vector3 tmpV = birdie.trajectoryPath.get(i);

					tmp.setToTranslation(tmpV.x, tmpV.y, tmpV.z);
					model.mul(tmp);

					tmp.setToScaling(0.1f, 0.1f, 0.1f);
					model.mul(tmp);

					tmp.setToLookAt(birdie.tangent, birdie.up);
					model.mul(tmp);

					tmp.setToRotation(Vector3.X, 90);
					model.mul(tmp);

					diffuseShader.setUniformMatrix("MMatrix", model);
					diffuseShader.setUniformi("uSampler", 0);

					diffuseShader.setUniformf("alpha", -Helper.map(i,
							trajectoryCnt - 20, trajectoryCnt, 1, 0));

					modelBirdieTex.bind(0);
					modelBirdieObj.render(diffuseShader);
				}

			}
		}
		diffuseShader.setUniformf("alpha", 1);
		Gdx.gl.glDisable(GL20.GL_BLEND);

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		// render shadow
		tmp.idt();
		model.idt();

		tmp.setToTranslation(birdie.currentPosition.x, 0.1f,
				birdie.currentPosition.y);
		model.mul(tmp);

		tmp.setToScaling(0.1f - birdie.currentPosition.z / 10,
				0.1f - birdie.currentPosition.z / 10,
				0.1f - birdie.currentPosition.z / 10);
		model.mul(tmp);

		diffuseShader.setUniformMatrix("MMatrix", model);
		diffuseShader.setUniformi("uSampler", 0);
		diffuseShader.setUniformf("alpha", birdie.currentPosition.z / 4f);

		modelShadowTex.bind(0);
		modelPlaneObj.render(diffuseShader);

		diffuseShader.setUniformf("alpha", 1);
		// render player
		tmp.idt();
		model.idt();

		tmp.setToRotation(Vector3.X, 90);
		model.mul(tmp);

		if (!player.jump)
			tmp.setToTranslation(player.position.x, player.position.y, -0.5f);
		else
			tmp.setToTranslation(player.position.x, player.position.y,
					player.position.z);
		model.mul(tmp);

		float scaler = 0.5f;
		
		if (player.state == Player.STATE.AIMING) {
			float help = (player.aimTime / 2) % 0.5f;

			if (help < 0.25f)
				scaler = 0.5f + help;
			if (help > 0.25f)
				scaler = 1 - help;
		}

		tmp.setToScaling(scaler, scaler, scaler);
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

		tmp.setToTranslation(opponent.position.x, opponent.position.y, -0.5f);
		model.mul(tmp);

		tmp.setToScaling(0.5f, 0.5f, 0.5f);
		model.mul(tmp);

		diffuseShader.setUniformMatrix("MMatrix", model);
		diffuseShader.setUniformi("uSampler", 0);

		modelPlaneTex.bind(0);
		modelPlaneObj.render(diffuseShader);

		// render stadium
		tmp.idt();
		model.idt();

		tmp.setToTranslation(0, 0, 0);
		model.mul(tmp);

		tmp.setToScaling(10, 10, 10);
		model.mul(tmp);

		tmp.setToRotation(Vector3.Y, -90);
		model.mul(tmp);

		diffuseShader.setUniformMatrix("MMatrix", model);
		diffuseShader.setUniformi("uSampler", 0);

		diffuseShader.setUniformf("alpha", 0.2f);

		modelStadiumTex.bind(0);
		modelStadiumObj.render(diffuseShader);

		Gdx.gl.glDisable(GL20.GL_BLEND);

		diffuseShader.end();

	}

}
