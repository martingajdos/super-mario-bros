package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setForegroundFPS(MarioGameTest.FRAMES);
		config.setWindowedMode(MarioGameTest.V_WIDTH * 3, MarioGameTest.V_HEIGHT * 3);
		config.setTitle("LD58");
		config.setResizable(false);

		new Lwjgl3Application(new MarioGameTest(), config);
	}
}
