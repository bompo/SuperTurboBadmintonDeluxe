package de.redlion.badminton.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g3d.AnimatedModelNode;
import com.badlogic.gdx.graphics.g3d.StillModelNode;
import com.badlogic.gdx.graphics.g3d.lights.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.lights.LightManager;
import com.badlogic.gdx.graphics.g3d.lights.LightManager.LightQuality;
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoaderRegistry;
import com.badlogic.gdx.graphics.g3d.materials.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.materials.MaterialAttribute;
import com.badlogic.gdx.graphics.g3d.model.keyframe.KeyframedAnimation;
import com.badlogic.gdx.graphics.g3d.model.keyframe.KeyframedModel;
import com.badlogic.gdx.graphics.g3d.model.still.StillModel;
import com.badlogic.gdx.graphics.g3d.test.PrototypeRendererGL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import de.redlion.badminton.Birdie;
import de.redlion.badminton.GameSession;
import de.redlion.badminton.Player;
import de.redlion.badminton.opponent.Opponent;

public class RenderStadium {
	
	static final int LIGHTS_NUM = 0;
	static final float LIGHT_INTESITY = 0f;
	
	boolean highQuality = true;
	
	static final String TAG = "Super Turbo Badminton";

	LightManager lightManager;

	float timer;
	float alpha = 1;
	PrototypeRendererGL20 protoRenderer;
	KeyframedModel modelOctopus;
	
	AnimatedModelNode instancePlayer;
	AnimatedModelNode instanceOpponent;
	StillModelNode instanceStadium;
	StillModelNode instanceBirdie;
	
	Material octopus;	
	
	StillModel modelPlane;
	Texture modelPlaneTex;

	StillModel modelBirdie;
	Texture modelBirdieTex;

	StillModel modelCourt;
	Texture modelCourtTex;

	StillModel modelStadium;
	StillModel modelWater;
	StillModel modelNet;
	Texture modelStadiumTex;

	Texture modelShadowTex;
	
	Birdie birdie;
	Player player;
	Opponent opponent;

	// GLES20
	Matrix4 model = new Matrix4().idt();
	Matrix4 normal = new Matrix4().idt();
	Matrix4 tmp = new Matrix4().idt();

	Preferences prefs;


	public RenderStadium() {
		prefs = Gdx.app.getPreferences("de.redlion.badminton");
		
		highQuality = prefs.getBoolean("highQuality", true);
		if(highQuality) {
			lightManager = new LightManager(LIGHTS_NUM, LightQuality.FRAGMENT);
		} else {
			lightManager = new LightManager(LIGHTS_NUM, LightQuality.VERTEX);
		}
		
		lightManager.dirLight = new DirectionalLight();
		lightManager.dirLight.color.set(1f, 1f, 1f, 1);
		lightManager.dirLight.direction.set(-.4f, -1, 0.03f).nor();

		lightManager.ambientLight.set(.0f, 0.0f, 0.0f, 0f);
		
		protoRenderer = new PrototypeRendererGL20(lightManager);
		
		modelPlane = ModelLoaderRegistry.loadStillModel(Gdx.files
				.internal("data/plane.g3dt"));
		modelPlaneTex = new Texture(
				Gdx.files.internal("data/court_top_texture.png"), true);
		modelPlaneTex.setFilter(TextureFilter.MipMapLinearLinear,
				TextureFilter.Linear);
		modelPlaneTex.getTextureData().useMipMaps();

		modelBirdie = ModelLoaderRegistry.loadStillModel(Gdx.files
				.internal("data/birdie.g3dt"));
		modelBirdieTex = new Texture(
				Gdx.files.internal("data/birdie_diff.png"), true);
		modelBirdieTex.setFilter(TextureFilter.MipMapLinearLinear,
				TextureFilter.Linear);
		modelBirdieTex.getTextureData().useMipMaps();

		modelCourt = ModelLoaderRegistry.loadStillModel(Gdx.files
				.internal("data/court.g3dt"));
		modelCourtTex = new Texture(Gdx.files.internal("data/court_only.png"),
				true);

		modelStadium = ModelLoaderRegistry.loadStillModel(Gdx.files
				.internal("data/stadium.g3dt"));
		modelStadiumTex = new Texture(Gdx.files.internal("data/uv_map.png"),
				true);
		modelStadiumTex.setFilter(TextureFilter.MipMapLinearLinear,
				TextureFilter.Linear);
		modelStadiumTex.getTextureData().useMipMaps();
		
		modelWater = ModelLoaderRegistry.loadStillModel(Gdx.files
				.internal("data/water.g3dt"));
		
		modelNet = ModelLoaderRegistry.loadStillModel(Gdx.files
				.internal("data/net.g3dt"));

		modelShadowTex = new Texture(Gdx.files.internal("data/shadow.png"),
				false);
		
		modelOctopus = ModelLoaderRegistry.loadKeyframedModel(Gdx.files
				.internal("data/octopus_anim.g3dt"));
		
		player = GameSession.getInstance().player;
		birdie = GameSession.getInstance().birdie;
		opponent = GameSession.getInstance().opponent;
		
		{
			// add stadium
			BoundingBox box = new BoundingBox();		
			instanceStadium = new StillModelNode();
			modelStadium.getBoundingBox(box);
			instanceStadium.matrix.trn(0f, 0f, 0f);
			instanceStadium.matrix.scale(1f, 1f, 1f);
			box.mul(instanceStadium.matrix);
			instanceStadium.radius = (box.getDimensions().len() / 2);
		}
		
		
		// set materials
		MaterialAttribute blueDiffuseColor = new ColorAttribute(new Color(0.1f, 0.2f, 0.91f, 1.0f), ColorAttribute.diffuse);
		MaterialAttribute yellowDiffuseColor = new ColorAttribute(new Color(1f, 0.7f, 0.0f, 1.0f), ColorAttribute.diffuse);
		MaterialAttribute redDiffuseColor = new ColorAttribute(new Color(0.8f, 0.0f, 0.3f, 1.0f), ColorAttribute.diffuse);
		MaterialAttribute whiteDiffuseColor = new ColorAttribute(new Color(1.0f, 1.0f, 1.0f, 1.0f), ColorAttribute.diffuse);
		MaterialAttribute blackDiffuseColor = new ColorAttribute(new Color(0.01f, 0.01f, 0.01f, 1.0f), ColorAttribute.diffuse);
		
		MaterialAttribute pureWhiteDiffuseColor = new ColorAttribute(new Color(0.99f, 0.99f, 0.99f, 1.0f), ColorAttribute.diffuse);
		
		MaterialAttribute blueSpecularColor = new ColorAttribute(new Color(0.0f, 0.04f, 0.49f, 1.0f), ColorAttribute.specular);
		MaterialAttribute yellowSpecularColor = new ColorAttribute(new Color(0.58f, 0.35f, 0.0f, 1.0f), ColorAttribute.specular);
		MaterialAttribute redSpecularColor = new ColorAttribute(new Color(0.2f, 0.0f, 0.03f, 1.0f), ColorAttribute.specular);
		MaterialAttribute whiteSpecularColor = new ColorAttribute(new Color(0.6f, 0.6f, 0.6f, 1.0f), ColorAttribute.specular);
		MaterialAttribute blackSpecularColor = new ColorAttribute(new Color(0.01f, 0.01f, 0.01f, 1.0f), ColorAttribute.specular);
		
		MaterialAttribute darkPurpleDiffuseColor = new ColorAttribute(new Color(0.3f, 0.0f, 0.2f, 1.0f), ColorAttribute.diffuse);
		MaterialAttribute lightPurpleDiffuseColor = new ColorAttribute(new Color(0.5f, 0.0f, 0.2f, 1.0f), ColorAttribute.diffuse);
		
		MaterialAttribute waterDiffuseColor = new ColorAttribute(new Color(0.2f, 0.45f, 0.6f, 0.6f), ColorAttribute.diffuse);
		MaterialAttribute alphaBlending = new BlendingAttribute("translucent");
		
		MaterialAttribute netDiffuseColor = new ColorAttribute(new Color(0.95f, 0.95f, 0.95f, 0.8f), ColorAttribute.diffuse);
		
		Material clayBlue = new Material("blue", blueDiffuseColor, blueSpecularColor);
		Material clayWhite = new Material("white", whiteDiffuseColor, whiteSpecularColor);
		Material clayBlack = new Material("black", blackDiffuseColor, blackSpecularColor);
		Material clayRed = new Material("red", redDiffuseColor, redSpecularColor);
		Material clayYellow = new Material("tentacle", yellowDiffuseColor, yellowSpecularColor);
		
		Material darkPurple = new Material("court", darkPurpleDiffuseColor);
		Material lightPurple = new Material("outerCourt", lightPurpleDiffuseColor);
		
		Material pureWhite = new Material("pureWhite", pureWhiteDiffuseColor);
		
		Material water = new Material("water", waterDiffuseColor, alphaBlending);
		Material net = new Material("net", netDiffuseColor, alphaBlending);
				
		modelOctopus.setMaterial(clayBlue);
		modelOctopus.getSubMesh("bandana").material = clayWhite;
		modelOctopus.getSubMesh("body").material = clayBlue;
		modelOctopus.getSubMesh("eye").material = clayWhite;
		modelOctopus.getSubMesh("pupile").material = clayBlack;
		modelOctopus.getSubMesh("suckers").material = clayRed;
		modelOctopus.getSubMesh("tentacles").material = clayYellow;
		
		modelStadium.setMaterial(pureWhite);
		modelStadium.getSubMesh("playground").material = lightPurple;
		modelStadium.getSubMesh("playfield").material = darkPurple;
		
		modelWater.setMaterial(water);
		modelNet.setMaterial(net);
		
		modelBirdie.setMaterial(pureWhite);	
	}
	
	public void updateCamera(PerspectiveCamera cam) {
		protoRenderer.cam = cam;
	}

	public void render() {
		final float delta = Gdx.graphics.getDeltaTime();
		
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);

		{
			//player
			float scaler = 1f;		
			if (player.state == Player.STATE.AIMING) {
				float help = (player.aimTime / 2) % 0.5f;
	
				if (help < 0.25f)
					scaler = 0.5f + help;
				if (help > 0.25f)
					scaler = 1 - help;
			}
			
			BoundingBox box = new BoundingBox();		
			instancePlayer = new AnimatedModelNode();
			modelOctopus.getBoundingBox(box);
			instancePlayer.matrix.trn(player.position.x, 0, player.position.y);
			instancePlayer.matrix.scale(scaler, scaler, scaler);
			box.mul(instancePlayer.matrix);
			instancePlayer.radius = (box.getDimensions().len() / 2);
			
			KeyframedAnimation[] animations = modelOctopus.getAnimations();
			instancePlayer.animation = animations[0].name;
			instancePlayer.time = 0;
			instancePlayer.looping = true;
			
			instancePlayer.time = player.keyframeAnimTime*0.7f;
			if (instancePlayer.time > modelOctopus.getAnimations()[0].totalDuration) {
				player.keyframeAnimTime = 0;
				instancePlayer.time = 0;
			}
		}
		
		{
			// opponent
			float scaler = 1f;
			if (opponent.state == Player.STATE.AIMING) {
				float help = (opponent.aimTime / 2) % 0.5f;
	
				if (help < 0.25f)
					scaler = 0.5f + help;
				if (help > 0.25f)
					scaler = 1 - help;
			}
			
			BoundingBox box = new BoundingBox();		
			instanceOpponent = new AnimatedModelNode();
			modelOctopus.getBoundingBox(box);
			instanceOpponent.matrix.rotate(Vector3.Y, 180);
			instanceOpponent.matrix.trn(opponent.position.x, 0, opponent.position.y);
			instanceOpponent.matrix.scale(scaler, scaler, scaler);
			box.mul(instancePlayer.matrix);
			instanceOpponent.radius = (box.getDimensions().len() / 2);
			
			KeyframedAnimation[] animations = modelOctopus.getAnimations();
			instanceOpponent.animation = animations[0].name;
			instanceOpponent.time = 0;
			instanceOpponent.looping = true;
			
			instanceOpponent.time = opponent.keyframeAnimTime*0.7f;
			if (instanceOpponent.time > modelOctopus.getAnimations()[0].totalDuration) {
				opponent.keyframeAnimTime = 0;
				instanceOpponent.time = 0;
			}
		}
		
		{
			// birdie	
			BoundingBox box = new BoundingBox();		
			instanceBirdie = new StillModelNode();
			modelOctopus.getBoundingBox(box);
			instanceBirdie.matrix.trn(birdie.currentPosition.x, birdie.currentPosition.z, birdie.currentPosition.y);
			instanceBirdie.matrix.scale(2, 2, 2);
			tmp.setToLookAt(birdie.tangent, birdie.up);
			instanceBirdie.matrix.mul(tmp);
			box.mul(instanceBirdie.matrix);
			instanceBirdie.radius = (box.getDimensions().len() / 2);
		}
			
		
		Gdx.gl.glDisable(GL10.GL_BLEND);
		Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);
		Gdx.gl.glDepthMask(true);
		
		protoRenderer.begin();
		protoRenderer.draw(modelStadium, instanceStadium);
		protoRenderer.draw(modelWater, instanceStadium);
		protoRenderer.draw(modelNet, instanceStadium);
		protoRenderer.draw(modelOctopus, instancePlayer);	
		protoRenderer.draw(modelOctopus, instanceOpponent);
		protoRenderer.draw(modelBirdie, instanceBirdie);
		protoRenderer.end();
		
//		// render birdie
//		tmp.idt();
//		model.idt();
//
//		tmp.setToRotation(Vector3.X, 90);
//		model.mul(tmp);
//
//		tmp.setToTranslation(birdie.currentPosition.x,
//				birdie.currentPosition.y, birdie.currentPosition.z);
//		model.mul(tmp);
//
//		tmp.setToScaling(0.1f, 0.1f, 0.1f);
//		model.mul(tmp);
//
//		tmp.setToLookAt(birdie.tangent, birdie.up);
//		model.mul(tmp);
//
//		tmp.setToRotation(Vector3.X, 90);
//		model.mul(tmp);
//
//		diffuseShader.setUniformMatrix("MMatrix", model);
//		diffuseShader.setUniformi("uSampler", 0);
//
//		modelBirdieTex.bind(0);
//		modelBirdieObj.render(diffuseShader);
//
//		// render birdie trajectory
//		Gdx.gl.glEnable(GL20.GL_BLEND);
//		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//		if (birdie.trajectoryPath.size > 0) {
//			int trajectoryCnt = birdie.trajectoryPath.indexOf(
//					birdie.currentPosition, false);
//			for (int i = trajectoryCnt; i > trajectoryCnt - 20; i--) {
//				if (i > 0) {
//					tmp.idt();
//					model.idt();
//
//					tmp.setToRotation(Vector3.X, 90);
//					model.mul(tmp);
//
//					Vector3 tmpV = birdie.trajectoryPath.get(i);
//
//					tmp.setToTranslation(tmpV.x, tmpV.y, tmpV.z);
//					model.mul(tmp);
//
//					tmp.setToScaling(0.1f, 0.1f, 0.1f);
//					model.mul(tmp);
//
//					tmp.setToLookAt(birdie.tangent, birdie.up);
//					model.mul(tmp);
//
//					tmp.setToRotation(Vector3.X, 90);
//					model.mul(tmp);
//
//					diffuseShader.setUniformMatrix("MMatrix", model);
//					diffuseShader.setUniformi("uSampler", 0);
//
//					diffuseShader.setUniformf("alpha", -Helper.map(i,
//							trajectoryCnt - 20, trajectoryCnt, 1, 0));
//
//					modelBirdieTex.bind(0);
//					modelBirdieObj.render(diffuseShader);
//				}
//
//			}
//		}
//		diffuseShader.setUniformf("alpha", 1);
//		Gdx.gl.glDisable(GL20.GL_BLEND);
//
//		Gdx.gl.glEnable(GL20.GL_BLEND);
//		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//
//		// render shadow
//		tmp.idt();
//		model.idt();
//
//		tmp.setToTranslation(birdie.currentPosition.x, 0.1f,
//				birdie.currentPosition.y);
//		model.mul(tmp);
//
//		tmp.setToScaling(0.1f - birdie.currentPosition.z / 10,
//				0.1f - birdie.currentPosition.z / 10,
//				0.1f - birdie.currentPosition.z / 10);
//		model.mul(tmp);
//
//		diffuseShader.setUniformMatrix("MMatrix", model);
//		diffuseShader.setUniformi("uSampler", 0);
//		diffuseShader.setUniformf("alpha", birdie.currentPosition.z / 4f);
//
//		modelShadowTex.bind(0);
//		modelPlaneObj.render(diffuseShader);
//
//		diffuseShader.setUniformf("alpha", 1);
//		
//		
//		// render player
//		{
//			tmp.idt();
//			model.idt();
//	
//			tmp.setToRotation(Vector3.X, 90);
//			model.mul(tmp);
//	
//		
//			tmp.setToTranslation(player.position.x, player.position.y, -0.5f);
//			model.mul(tmp);
//	
//			float scaler = 0.5f;
//			
//			if (player.state == Player.STATE.AIMING) {
//				float help = (player.aimTime / 2) % 0.5f;
//	
//				if (help < 0.25f)
//					scaler = 0.5f + help;
//				if (help > 0.25f)
//					scaler = 1 - help;
//			}
//	
//			tmp.setToScaling(scaler, scaler, scaler);
//			model.mul(tmp);
//	
//			diffuseShader.setUniformMatrix("MMatrix", model);
//			diffuseShader.setUniformi("uSampler", 0);
//	
//			modelPlaneTex.bind(0);
//			modelPlaneObj.render(diffuseShader);
//		}
//
//		// render opponent
//		{
//			tmp.idt();
//			model.idt();
//	
//			tmp.setToRotation(Vector3.X, 90);
//			model.mul(tmp);
//	
//			tmp.setToTranslation(opponent.position.x, opponent.position.y, -0.5f);
//			model.mul(tmp);
//	
//			float scaler = 0.5f;
//			
//			if (opponent.state == Player.STATE.AIMING) {
//				float help = (opponent.aimTime / 2) % 0.5f;
//	
//				if (help < 0.25f)
//					scaler = 0.5f + help;
//				if (help > 0.25f)
//					scaler = 1 - help;
//			}
//	
//			tmp.setToScaling(scaler, scaler, scaler);
//			model.mul(tmp);
//			
//			diffuseShader.setUniformMatrix("MMatrix", model);
//			diffuseShader.setUniformi("uSampler", 0);
//	
//			modelPlaneTex.bind(0);
//			modelPlaneObj.render(diffuseShader);
//		}
//
//		// render stadium
//		tmp.idt();
//		model.idt();
//
//		tmp.setToTranslation(0, 0, 0);
//		model.mul(tmp);
//
//		tmp.setToScaling(10, 10, 10);
//		model.mul(tmp);
//
//		tmp.setToRotation(Vector3.Y, -90);
//		model.mul(tmp);
//
//		diffuseShader.setUniformMatrix("MMatrix", model);
//		diffuseShader.setUniformi("uSampler", 0);
//
//		diffuseShader.setUniformf("alpha", 0.2f);
//
//		modelStadiumTex.bind(0);
//		modelStadiumObj.render(diffuseShader);
//
//		Gdx.gl.glDisable(GL20.GL_BLEND);
//
//		diffuseShader.end();

	}

}
