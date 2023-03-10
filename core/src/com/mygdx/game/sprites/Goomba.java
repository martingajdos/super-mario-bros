package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MarioGameTest;
import com.mygdx.game.screens.PlayScreen;

public class Goomba extends Enemy {

    // set up for animations
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;

    private boolean setToDestroy;
    private boolean destroyed;

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        frames = new Array<TextureRegion>();
        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
        }
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;

        setBounds(getX(), getY(), 16 / MarioGameTest.PPM, 16 / MarioGameTest.PPM);

        setToDestroy = false;
        destroyed = false;
    }

    public void tick(float dt) {
        stateTime += dt;

        // destroy the body
        if (setToDestroy && !destroyed) {
            world.destroyBody(body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
            stateTime = 0;
        } else if (!destroyed) {

            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));

            body.setLinearVelocity(this.velocity);
        }
    }

    @Override
    protected void defineEnemy() {
        // Body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        // Create body in the world
        body = world.createBody(bodyDef);

        // Fixture and shape
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioGameTest.PPM);

        // Fixture category bits and mask bits
        fixtureDef.filter.categoryBits = MarioGameTest.ENEMY_BIT;
        fixtureDef.filter.maskBits = MarioGameTest.GROUND_BIT | MarioGameTest.COIN_BIT | MarioGameTest.BRICK_BIT |
                MarioGameTest.ENEMY_BIT | MarioGameTest.OBJECT_BIT | MarioGameTest.MARIO_BIT;

        fixtureDef.shape = shape;

        // Create the fixture in body
        body.createFixture(fixtureDef).setUserData(this);

        // create the Goomba's head (so that Mario can jump on it)
        PolygonShape head = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-5, 8).scl(1 / MarioGameTest.PPM);
        vertices[1] = new Vector2(5, 8).scl(1 / MarioGameTest.PPM);
        vertices[2] = new Vector2(-3, 3).scl(1 / MarioGameTest.PPM);
        vertices[3] = new Vector2(3, 3).scl(1 / MarioGameTest.PPM);
        head.set(vertices);

        // setting the shape and restitution (bounciness)
        fixtureDef.shape = head;
        fixtureDef.restitution = 0.5f;
        // set the fixture's category bit to ENEMY_HEAD_BIT
        fixtureDef.filter.categoryBits = MarioGameTest.ENEMY_HEAD_BIT;
        // creating the fixture of a Goomba's head & setting the user data, so that we have access to this class during collision
        body.createFixture(fixtureDef).setUserData(this);
    }

    // we are overriding the Sprite's draw method to be able to not draw the goomba after it's destroyed
    @Override
    public void draw(Batch batch) {
        if (!destroyed || stateTime < 1) {
            super.draw(batch);
        }
    }

    @Override
    public void hitOnHead() {
        setToDestroy = true;
    }
}
