package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.screens.PlayScreen;

public class MarioGameTest extends Game {
	public static final int FRAMES = 60;
	public static final int V_WIDTH = 400, V_HEIGHT = 208;
	public static final float PPM = 100;

	// default values for filters
	public static final short DEFAULT_BIT = 1; // every fixture that is created already has a filter category set to 1 (DEFAULT_BIT)
	public static final short MARIO_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROYED_BIT = 16;

	// for managing sounds and music, we use AssetManager (not recommended to be used statically, oh well ;) )
	public static AssetManager manager;

	public SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();

		// load up all the sound and music files
		manager = new AssetManager();
		manager.load("audio/music/mario_music.ogg", Music.class);
		manager.load("audio/sounds/coin.wav", Sound.class);
		manager.load("audio/sounds/bump.wav", Sound.class);
		manager.load("audio/sounds/breakblock.wav", Sound.class);

		// Async loading, blocks everything until assets are loaded
		manager.finishLoading();

		this.setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		manager.dispose();
		batch.dispose();
	}
}
