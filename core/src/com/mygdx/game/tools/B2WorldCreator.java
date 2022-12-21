package com.mygdx.game.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.MarioGameTest;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.sprites.Brick;
import com.mygdx.game.sprites.Coin;

public class B2WorldCreator {
    public B2WorldCreator(PlayScreen screen) {
        initBox2D(screen);
    }

    private void initBox2D(PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        // Before creating a body, we need to define it with BodyDef
        BodyDef bodyDef = new BodyDef();

        // Next we need to define a shape (Polygon shape) for the body's FIXTURE
        PolygonShape shape = new PolygonShape();

        // Before creating a body's fixture, we need to define it with FixtureDef, then we ADD the fixture to the BODY
        FixtureDef fixtureDef = new FixtureDef();

        // Actual body
        Body body;

        // Go through all the objects in the map, that are of RectangleMapObject type and are in the 2.nd layer
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            // Get a particular rectangle from the current object
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / MarioGameTest.PPM,
                    (rectangle.getY() + rectangle.getHeight() / 2) / MarioGameTest.PPM);

            body = world.createBody(bodyDef);

            shape.setAsBox(rectangle.getWidth()/2/ MarioGameTest.PPM,rectangle.getHeight()/2/ MarioGameTest.PPM);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        }

        for (RectangleMapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = object.getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / MarioGameTest.PPM,
                    (rectangle.getY() + rectangle.getHeight() / 2) / MarioGameTest.PPM);

            body = world.createBody(bodyDef);

            shape.setAsBox(rectangle.getWidth()/2/ MarioGameTest.PPM,rectangle.getHeight()/2/ MarioGameTest.PPM);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        }

        for (RectangleMapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = object.getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / MarioGameTest.PPM,
                    (rectangle.getY() + rectangle.getHeight() / 2) / MarioGameTest.PPM);

            body = world.createBody(bodyDef);

            shape.setAsBox(rectangle.getWidth()/2/ MarioGameTest.PPM,rectangle.getHeight()/2/ MarioGameTest.PPM);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        }

        for (RectangleMapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = object.getRectangle();
            new Coin(screen, rectangle);
        }

        for (RectangleMapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = object.getRectangle();
            new Brick(screen, rectangle);
        }
    }
}
