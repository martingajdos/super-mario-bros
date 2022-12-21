package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.MarioGameTest;

public class Mario extends Sprite {
    public World world;
    public Body body;

    public Mario(World world) {
        this.world = world;
        defineMario();
    }

    private void defineMario() {
        // Body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / MarioGameTest.PPM, 32 / MarioGameTest.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        // Create body in the world
        body = world.createBody(bodyDef);

        // Fixture and shape
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / MarioGameTest.PPM);
        fixtureDef.shape = shape;

        // Create the fixture in body
        body.createFixture(fixtureDef);
    }
}
