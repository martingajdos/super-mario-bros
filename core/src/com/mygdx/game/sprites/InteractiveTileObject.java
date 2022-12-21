package com.mygdx.game.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.MarioGameTest;

public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;

    // tile's fixture
    protected Fixture fixture;

    public InteractiveTileObject(World world, TiledMap map, Rectangle bounds) {
        this.world = world;
        this.map = map;
        this.bounds = bounds;

        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((bounds.getX() + bounds.getWidth() / 2) / MarioGameTest.PPM,
                (bounds.getY() + bounds.getHeight() / 2) / MarioGameTest.PPM);

        body = world.createBody(bodyDef);

        shape.setAsBox(bounds.getWidth()/2/ MarioGameTest.PPM,bounds.getHeight()/2/ MarioGameTest.PPM);
        fixtureDef.shape = shape;
        // capture a tile's fixture into a variable so that we can access it later
        fixture = body.createFixture(fixtureDef);
    }

    public abstract void onHeadHit();

    // each InteractiveTileObject will have set a category filter which we define
    // and that filter will be added to the object's fixture
    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    // gets a particular cell (defined in Tiled) where a InteractiveTileObject is located, so we know
    // at which position to destroy the graphics texture
    // All these objects in Tiled are drawn in the Graphics Layer (meaning id=1). Yes, for ex. bricks or coins
    // have their own layer, but that layer is only used for the box2d collision box, not for its texture. That is
    // in the Graphics Layer (1)
    public TiledMapTileLayer.Cell getCell() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);

        // we return the layer in a position, where:
        // we get the X & Y position of the object's body,
        // multiply it by the Pixels Per Meter (because we scaled it down before),
        // and divide that by the TILE_SIZE (16)
        return layer.getCell((int)(body.getPosition().x * MarioGameTest.PPM / 16),
                             (int)(body.getPosition().y * MarioGameTest.PPM / 16));
    }
}
