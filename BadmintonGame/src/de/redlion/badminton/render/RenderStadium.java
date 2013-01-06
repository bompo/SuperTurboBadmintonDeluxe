package de.redlion.badminton.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g3d.AnimatedModelNode;
import com.badlogic.gdx.graphics.g3d.StillModelNode;
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoaderRegistry;
import com.badlogic.gdx.graphics.g3d.loaders.g3d.chunks.G3dExporter;
import com.badlogic.gdx.graphics.g3d.materials.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.materials.MaterialAttribute;
import com.badlogic.gdx.graphics.g3d.materials.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.keyframe.KeyframedAnimation;
import com.badlogic.gdx.graphics.g3d.model.keyframe.KeyframedModel;
import com.badlogic.gdx.graphics.g3d.model.still.StillModel;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import de.redlion.badminton.Birdie;
import de.redlion.badminton.GameSession;
import de.redlion.badminton.Player;
import de.redlion.badminton.opponent.Opponent;
import de.redlion.badminton.render.LightManager.LightQuality;

public class RenderStadium {
	
	static final int LIGHTS_NUM = 0;
	static final float LIGHT_INTESITY = 0f;
	
	boolean highQuality = true;
	
	static final String TAG = "de.redlion.badminton";

	LightManager lightManager;

	float timer;
	float alpha = 1;
	PrototypeRendererGL20 protoRenderer;
	KeyframedModel modelOctopus;
	KeyframedModel modelElephant;
	
	AnimatedModelNode instancePlayer;
	AnimatedModelNode instanceOpponent;
	StillModelNode instanceStadium;
	StillModelNode instanceBirdie;
	StillModelNode instanceBirdieShadow;
	
	BoundingBox instancePlayerBB;
	BoundingBox instanceOpponentBB;
	
	Material octopus;
	Material elephant;
	
	StillModel modelPlane;
	Texture modelPlaneTex;

	StillModel modelBirdie;
	Texture modelBirdieTex;
	
	StillModel modelBirdieShadow;
	Texture modelBirdieShadowTex;

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

	Matrix4 tmp = new Matrix4().idt();

	Preferences prefs;


	public RenderStadium() {
		prefs = Gdx.app.getPreferences(TAG);
		
		highQuality = prefs.getBoolean("highQuality", false);
		if(highQuality) {
			lightManager = new LightManager(LIGHTS_NUM, LightQuality.FRAGMENT);
		} else {
			lightManager = new LightManager(LIGHTS_NUM, LightQuality.VERTEX);
		}
		
		lightManager.dirLight = new DirectionalLight();
		lightManager.dirLight.color.set(1f, 1f, 1f, 1);
		lightManager.dirLight.direction.set(-0.4f, -1.0f, 0.03f).nor();

		lightManager.ambientLight.set(0.0f, 0.0f, 0.0f, 0.0f);
		
		protoRenderer = new PrototypeRendererGL20(lightManager);

		modelBirdie = ModelLoaderRegistry.loadStillModel(Gdx.files
				.internal("data/birdie.g3dt"));
		modelBirdieTex = new Texture(
				Gdx.files.internal("data/birdie_diff.png"), true);
		modelBirdieTex.setFilter(TextureFilter.MipMapLinearLinear,
				TextureFilter.Linear);
		modelBirdieTex.getTextureData().useMipMaps();
		
		modelBirdieShadow = ModelLoaderRegistry.loadStillModel(Gdx.files
				.internal("data/birdie_shadow.g3dt"));
		modelBirdieShadowTex = new Texture(
				Gdx.files.internal("data/shadow.png"), false);
		
		if(Gdx.files.internal("data/stadium.g3d").exists()) {
			modelStadium = ModelLoaderRegistry.loadStillModel(Gdx.files.internal("data/stadium.g3d"));
		} else {
			modelStadium = ModelLoaderRegistry.loadStillModel(Gdx.files.internal("data/stadium.g3dt"));
			G3dExporter.export(modelStadium, Gdx.files.absolute("data/stadium.g3d"));
		}
		
		
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
		
		if(Gdx.files.internal("data/octopus_anim.g3d").exists()) {
			modelOctopus = ModelLoaderRegistry.loadKeyframedModel(Gdx.files.internal("data/octopus_anim.g3d"));
		} else {
			modelOctopus = ModelLoaderRegistry.loadKeyframedModel(Gdx.files.internal("data/octopus_anim.g3dt"));
			G3dExporter.export(modelOctopus, Gdx.files.absolute("data/octopus_anim.g3d"));
		}
		
		if(Gdx.files.internal("data/elephant_anim.g3d").exists()) {
			modelElephant = ModelLoaderRegistry.loadKeyframedModel(Gdx.files.internal("data/elephant_anim.g3d"));
		} else {
			modelElephant = ModelLoaderRegistry.loadKeyframedModel(Gdx.files.internal("data/elephant_anim.g3dt"));
			G3dExporter.export(modelElephant, Gdx.files.absolute("data/elephant_anim.g3d"));
		}
		
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
		MaterialAttribute lightRedDiffuseColor = new ColorAttribute(new Color(0.7f, 0.02f, 0.01f, 1.0f), ColorAttribute.diffuse);
		MaterialAttribute whiteDiffuseColor = new ColorAttribute(new Color(1.0f, 1.0f, 1.0f, 1.0f), ColorAttribute.diffuse);
		MaterialAttribute blackDiffuseColor = new ColorAttribute(new Color(0.01f, 0.01f, 0.01f, 1.0f), ColorAttribute.diffuse);
		MaterialAttribute brownDiffuseColor = new ColorAttribute(new Color(1.0f, 0.35f, 0.00f, 1.0f), ColorAttribute.diffuse);
		
		MaterialAttribute pureWhiteDiffuseColor = new ColorAttribute(new Color(0.99f, 0.99f, 0.99f, 1.0f), ColorAttribute.diffuse);
		
		MaterialAttribute blueSpecularColor = new ColorAttribute(new Color(0.0f, 0.04f, 0.49f, 1.0f), ColorAttribute.specular);
		MaterialAttribute yellowSpecularColor = new ColorAttribute(new Color(0.58f, 0.35f, 0.0f, 1.0f), ColorAttribute.specular);
		MaterialAttribute redSpecularColor = new ColorAttribute(new Color(0.2f, 0.0f, 0.03f, 1.0f), ColorAttribute.specular);
		MaterialAttribute lightRedSpecularColor = new ColorAttribute(new Color(0.2f, 0.0f, 0.03f, 1.0f), ColorAttribute.specular);
		MaterialAttribute whiteSpecularColor = new ColorAttribute(new Color(0.6f, 0.6f, 0.6f, 1.0f), ColorAttribute.specular);
		MaterialAttribute blackSpecularColor = new ColorAttribute(new Color(0.01f, 0.01f, 0.01f, 1.0f), ColorAttribute.specular);
		MaterialAttribute brownSpecularColor = new ColorAttribute(new Color(0.5f, 0.25f, 0.00f, 1.0f), ColorAttribute.specular);
		
		MaterialAttribute darkPurpleDiffuseColor = new ColorAttribute(new Color(0.3f, 0.0f, 0.2f, 1.0f), ColorAttribute.diffuse);
		MaterialAttribute lightPurpleDiffuseColor = new ColorAttribute(new Color(0.5f, 0.0f, 0.2f, 1.0f), ColorAttribute.diffuse);
		
		MaterialAttribute waterDiffuseColor = new ColorAttribute(new Color(0.2f, 0.45f, 0.6f, 0.6f), ColorAttribute.diffuse);
		MaterialAttribute waterSpecularColor = new ColorAttribute(new Color(0.2f, 0.45f, 0.6f, 1.0f), ColorAttribute.rim);
		MaterialAttribute waterAnimationFlag = new AnimateAttribute("water");
		MaterialAttribute alphaBlending = new BlendingAttribute("translucent");
		
		MaterialAttribute netDiffuseColor = new ColorAttribute(new Color(0.95f, 0.95f, 0.95f, 0.8f), ColorAttribute.diffuse);
		
		MaterialAttribute birdieShadowColor = new TextureAttribute(modelBirdieShadowTex, 0, TextureAttribute.diffuseTexture);
		
		Material clayBlue = new Material("blue", blueDiffuseColor, blueSpecularColor);
		Material clayWhite = new Material("white", whiteDiffuseColor, whiteSpecularColor);
		Material clayBlack = new Material("black", blackDiffuseColor, blackSpecularColor);
		Material clayRed = new Material("red", redDiffuseColor, redSpecularColor);
		Material clayLightRed = new Material("lightRed", lightRedDiffuseColor, lightRedSpecularColor);
		Material clayYellow = new Material("yellow", yellowDiffuseColor, yellowSpecularColor);
		Material clayBrown = new Material("brown", brownDiffuseColor, brownSpecularColor);
		
		Material darkPurple = new Material("court", darkPurpleDiffuseColor);
		Material lightPurple = new Material("outerCourt", lightPurpleDiffuseColor);
		
		Material pureWhite = new Material("pureWhite", pureWhiteDiffuseColor);
		
		Material water = new Material("water", waterDiffuseColor, waterSpecularColor, waterAnimationFlag, alphaBlending);
		Material net = new Material("net", netDiffuseColor, alphaBlending);
		
		Material birdieShadow = new Material("birdieShadow", birdieShadowColor);
				
		modelOctopus.setMaterial(clayBlue);
		modelOctopus.getSubMesh("bandana").material = clayWhite;
		modelOctopus.getSubMesh("body").material = clayBlue;
		modelOctopus.getSubMesh("eye").material = clayWhite;
		modelOctopus.getSubMesh("pupile").material = clayBlack;
		modelOctopus.getSubMesh("suckers").material = clayRed;
		modelOctopus.getSubMesh("tentacles").material = clayYellow;
		
		modelElephant.setMaterial(clayYellow);
		modelElephant.getSubMesh("bandana").material = clayLightRed;
		modelElephant.getSubMesh("pupille").material = clayBlack;
		modelElephant.getSubMesh("horn").material = clayBrown;
		modelElephant.getSubMesh("fingers").material = clayRed;
		modelElephant.getSubMesh("eyes").material = clayWhite;
		
		modelStadium.setMaterial(pureWhite);
		modelStadium.getSubMesh("playground").material = lightPurple;
		modelStadium.getSubMesh("playfield").material = darkPurple;
		
		modelWater.setMaterial(water);
		modelNet.setMaterial(net);
		
		modelBirdie.setMaterial(pureWhite);
		
		modelBirdieShadow.setMaterial(birdieShadow);	
		
		
		// create instances
		{
			instancePlayerBB = new BoundingBox();		
			instancePlayer = new AnimatedModelNode();
			modelOctopus.getBoundingBox(instancePlayerBB);
			instancePlayer.matrix.trn(player.position.x, player.position.y, player.position.z);
			instancePlayerBB.mul(instancePlayer.matrix);
			instancePlayer.radius = (instancePlayerBB.getDimensions().len() / 2);
			
			KeyframedAnimation[] animations = modelOctopus.getAnimations();
			instancePlayer.animation = animations[0].name;
			instancePlayer.time = MathUtils.random(animations[0].totalDuration);
			instancePlayer.looping = true;
		}
		
		{
			instanceOpponentBB = new BoundingBox();		
			instanceOpponent = new AnimatedModelNode();
			modelElephant.getBoundingBox(instanceOpponentBB);
			instanceOpponent.matrix.trn(opponent.position.x, opponent.position.y, opponent.position.z);
			instanceOpponentBB.mul(instanceOpponent.matrix);
			instanceOpponent.radius = (instanceOpponentBB.getDimensions().len() / 2);
			
			KeyframedAnimation[] animations = modelElephant.getAnimations();
			instanceOpponent.animation = animations[0].name;
			instanceOpponent.time = MathUtils.random(animations[0].totalDuration);
			instanceOpponent.looping = true;
		}
		
		
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
			instancePlayer.matrix.idt();
			instancePlayer.matrix.trn(player.position.x, player.position.y, player.position.z);
//			instancePlayer.matrix.scale(scaler, scaler, scaler);
			instancePlayerBB.mul(instancePlayer.matrix);
			
			instancePlayer.time = player.keyframeAnimTime;
			if (instancePlayer.time > modelOctopus.getAnimations()[0].totalDuration - 0.3f) {
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
			
			instanceOpponent.matrix.idt();
//			instanceOpponent.matrix.rotate(Vector3.Y, 180);
			instanceOpponent.matrix.trn(opponent.position.x, opponent.position.y, opponent.position.z);
//			instanceOpponent.matrix.scale(scaler, scaler, scaler);
			instanceOpponentBB.mul(instanceOpponent.matrix);
			
			instanceOpponent.time = opponent.keyframeAnimTime;
			if (instanceOpponent.time > modelElephant.getAnimations()[0].totalDuration - 0.3f) {
				opponent.keyframeAnimTime = 0;
				instanceOpponent.time = 0;
			}
		}
		
		{
			// birdie	
			BoundingBox box = new BoundingBox();		
			instanceBirdie = new StillModelNode();
			modelBirdie.getBoundingBox(box);
			instanceBirdie.matrix.rotate(Vector3.X, -90);
			instanceBirdie.matrix.trn(birdie.currentPosition.x, birdie.currentPosition.y, birdie.currentPosition.z);
			instanceBirdie.matrix.scale(1, 1, 1);
			tmp.setToLookAt(birdie.tangent, birdie.up);
			instanceBirdie.matrix.mul(tmp);
			box.mul(instanceBirdie.matrix);
			instanceBirdie.radius = (box.getDimensions().len() / 2);
		}
		
		{
			// birdie shadow
			BoundingBox box = new BoundingBox();		
			instanceBirdieShadow = new StillModelNode();
			modelBirdieShadow.getBoundingBox(box);
			instanceBirdieShadow.matrix.trn(birdie.currentPosition.x, 0.1f, birdie.currentPosition.z);
			instanceBirdieShadow.matrix.scale(1, 1, 1);
			box.mul(instanceBirdieShadow.matrix);
			instanceBirdieShadow.radius = (box.getDimensions().len() / 2);
		}
		
		protoRenderer.begin();
		protoRenderer.draw(modelStadium, instanceStadium);
		protoRenderer.draw(modelWater, instanceStadium);
		protoRenderer.draw(modelNet, instanceStadium);
		protoRenderer.draw(modelOctopus, instancePlayer);	
		protoRenderer.draw(modelElephant, instanceOpponent);
		protoRenderer.draw(modelBirdie, instanceBirdie);
		protoRenderer.draw(modelBirdieShadow, instanceBirdieShadow);
		protoRenderer.end();	

	}

}
