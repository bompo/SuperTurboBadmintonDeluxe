package de.redlion.badminton;

import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {		
		DisplayMode displayMode = LwjglApplicationConfiguration.getDesktopDisplayMode();
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.setFromDisplayMode(displayMode);

		config.width = 800;
		config.height = 480;
		config.title = "Super Turbo Badminton Double Edition";

		config.fullscreen = false;
		config.samples = 4;
		config.useGL20 = true;
		config.vSyncEnabled = true;
		config.useCPUSynch = true;
		
		new LwjglApplication(new Badminton(), config);
	}
}
