package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MarioGameTest;
import com.mygdx.game.gui.Hud;
import com.mygdx.game.screens.PlayScreen;

public class Brick extends InteractiveTileObject {
    public Brick(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        // set coin's fixture user data to this class (we can reference a tile by class then)
        fixture.setUserData(this);

        // set the brick fixture's category filter to BRICK_BIT
        setCategoryFilter(MarioGameTest.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Brick", "Collision");

        // after the brick collides with the Mario's head, change brick fixture's category filter to DESTROYED
        // meaning this particular brick will not be able to collide with Mario anymore
        // because we set what particular categories of fixture filters Mario can collide with in Mario class
        setCategoryFilter(MarioGameTest.DESTROYED_BIT);

        // set the tile at Bricks position to NULL within the TiledMap, we DELETE the brick graphics
        // we get the particular cell which we collided with (getCell), that returns back the TiledMapTileLayer object
        // and on that object, we can set a particular Tile to something (in this case NULL removes it)
        getCell().setTile(null);

        // add 200 to our score
        Hud.addScore(200);

        // play sound
        MarioGameTest.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
    }
}
