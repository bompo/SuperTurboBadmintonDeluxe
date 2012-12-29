package de.redlion.badminton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Application.ApplicationType;

public class Configuration {
	
	public Preferences preferences;
	public boolean fullscreen;
	public float brighness = 0.0f;
	public boolean debug = true;
	
	static Configuration instance;
	
	private Configuration() {
		preferences = Gdx.app.getPreferences("de.redlion.badminton");
		loadConfig();
	}
	
	private void loadConfig() {
		fullscreen = preferences.getBoolean("fullscreen", false);
	}
	
	public void setConfiguration() {
		if(Gdx.app.getType() == ApplicationType.Desktop) {
			if(fullscreen) {
				Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);
			} else {
				Gdx.graphics.setDisplayMode(800,480, false);		
			}
		}
	}
	
	public void setFullscreen(boolean onOff) {
		preferences.putBoolean("fullscreen", onOff);
		fullscreen = onOff;
		preferences.flush();
	}
		
	public void setDebug(boolean onOff) {
		preferences.putBoolean("debug", onOff);
		debug = onOff;
		preferences.flush();
	}
	
	public static Configuration getInstance() {
		if(instance!=null) return instance;
		instance = new Configuration();		
		return instance;
	}	
	


}
