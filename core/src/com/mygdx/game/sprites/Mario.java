package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MarioGameTest;
import com.mygdx.game.screens.PlayScreen;

public class Mario extends Sprite {

    // States
    public enum State {
        FALLING,
        JUMPING,
        STANDING,
        RUNNING
    }

    public State currentState;
    public State previousState;

    // Animations
    private Animation marioRun;
    private Animation marioJump;

    // For animations to determine to flip
    private boolean runningRight;

    // Counts any given time in a state
    private float stateTimer;

    public World world;
    public Body body;
    private TextureRegion marioStand;

    public Mario(PlayScreen screen) {
        super(screen.getAtlas().findRegion("little_mario"));
        this.world = screen.getWorld();

        // init states
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        // load animations
        // create an array of texture regions to pass to constructor
        Array<TextureRegion> frames = new Array<TextureRegion>();

        // first running texture starts at 16px, so that's why for loop i = 1 => 1*16 = 16
        // running animation ends at the position of a texture that is 4
        for (int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(getTexture(), i * 16, 10, 16, 16));
        }

        // create the run animation with those frames we loaded (0.1f - duration)
        marioRun = new Animation(0.1f, frames);
        // clear the array since we already loaded run animation. Jump anim. will be next (4 - 5)
        frames.clear();

        for (int i = 4; i < 6; i++) {
            frames.add(new TextureRegion(getTexture(), i * 16, 10, 16, 16));
        }
        marioJump = new Animation(0.1f, frames);

        defineMario();

        // get first sprite from the implemented Sprite texture, 16 by 16 wide
        marioStand = new TextureRegion(getTexture(), 0, 10, 16, 16);

        // Sets the position and size of the sprite when drawn, before scaling and rotation are applied.
        setBounds(0, 0, 16 / MarioGameTest.PPM, 16 / MarioGameTest.PPM);

        // region is actual texture that is associated with this sprite
        setRegion(marioStand);
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
        shape.setRadius(6 / MarioGameTest.PPM);

        // set Mario's fixture category to MARIO_BIT (2)
        fixtureDef.filter.categoryBits = MarioGameTest.MARIO_BIT;
        // set what kinds of fixtures can Mario collide with (if any of the InteractiveTileObjects fixture filters becomes anything different than this,
        // Mario won't have a collision with that object
        fixtureDef.filter.maskBits = MarioGameTest.GROUND_BIT | MarioGameTest.COIN_BIT | MarioGameTest.BRICK_BIT;

        fixtureDef.shape = shape;

        // Create the fixture in body
        body.createFixture(fixtureDef);

        // create Mario's head (for collision with objects, line between 2 points)
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioGameTest.PPM, 6 / MarioGameTest.PPM),
                  new Vector2(2 / MarioGameTest.PPM, 6 / MarioGameTest.PPM));
        fixtureDef.shape = head;
        fixtureDef.isSensor = true; // it no longer collides within the box2d world
        body.createFixture(fixtureDef).setUserData("head");
    }

    public void tick(float dt) {
        // set the position of the texture
        // body.getPosition().x - pos of fixture
        // getWidth() - width of the sprite (because of extending the sprite) - parent class
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);

//        setPosition(body.getPosition().x - ((getWidth() / 2) + 2 / MarioGameTest.PPM),
//                body.getPosition().y - ((getHeight() / 2) - 1 / MarioGameTest.PPM));

        setRegion(getFrame(dt));
    }

    // returns appropriate texture to render during animation
    private TextureRegion getFrame(float dt) {
        currentState = getState();

        // set a texture region according to Mario's state
        TextureRegion region;

        switch (currentState) {
            case JUMPING:
                region = (TextureRegion) marioJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                // true there means loop it
                region = (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioStand;
                break;
        }

        // if he's running to the left and the texture is not flipped left, flip it left
        if ((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        // if the current state doesnt equal to the previous state (we change states), add deltaTime to the stateTimer, else it's 0
        stateTimer = currentState == previousState ? stateTimer + dt : 0;

        // set previous state to the current one
        previousState = currentState;

        // return the TextureRegion which is adequate
        return region;
    }

    private State getState() {
        // checking the velocities and determining the state
        if (body.getLinearVelocity().y > 0 || (body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
            return State.JUMPING;
        } else if (body.getLinearVelocity().y < 0) {
            return State.FALLING;
        } else if (body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }
}
