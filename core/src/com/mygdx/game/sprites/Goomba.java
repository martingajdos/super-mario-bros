package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MarioGameTest;
import com.mygdx.game.screens.PlayScreen;

public class Goomba extends Enemy {

    // set up for animations
    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        frames = new Array<TextureRegion>();
        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
        }
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
    }

    @Override
    protected void defineEnemy() {
        // Body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / MarioGameTest.PPM, 32 / MarioGameTest.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        // Create body in the world
        body = world.createBody(bodyDef);

        // Fixture and shape
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioGameTest.PPM);

        fixtureDef.filter.categoryBits = MarioGameTest.ENEMY_BIT;
        fixtureDef.filter.maskBits = MarioGameTest.GROUND_BIT |
                                     MarioGameTest.COIN_BIT |
                                     MarioGameTest.BRICK_BIT |
                                     MarioGameTest.ENEMY_BIT |
                                     MarioGameTest.OBJECT_BIT;

        fixtureDef.shape = shape;

        // Create the fixture in body
        body.createFixture(fixtureDef);
    }
}
