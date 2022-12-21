package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MarioGameTest;
import com.mygdx.game.gui.Hud;

public class Coin extends InteractiveTileObject {
    // fetch the actual whole tileset (sprite sheet) to be accessible in the Coin object
    // so that we can dynamically change the coin object texture
    private static TiledMapTileSet tileSet;

    // ID of the sprite within the tileSet (look it up through Tiled) for the blank coin
    private final int BLANK_COIN = 28;

    public Coin(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        // set coin's fixture user data to this class (we can reference a tile by class then)
        fixture.setUserData(this);

        // set the coin fixture's category filter to COIN_BIT
        setCategoryFilter(MarioGameTest.COIN_BIT);

        // get the tileset which we use
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Coin", "Collision");

        if (getCell().getTile().getId() == BLANK_COIN) {
            MarioGameTest.manager.get("audio/sounds/bump.wav", Sound.class).play();
        } else {
            MarioGameTest.manager.get("audio/sounds/coin.wav", Sound.class).play();
        }

        // after Mario head collision, change the sprite of this object to BLANK COIN, which we
        // fetched from the spritesheet - tileset_gutter
        getCell().setTile(tileSet.getTile(BLANK_COIN));

        // add score
        Hud.addScore(100);
    }
}
