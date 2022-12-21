package com.mygdx.game;

import com.badlogic.gdx.Game;
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

	public SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
	}
}
